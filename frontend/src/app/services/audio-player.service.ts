import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface SoundTrack {
  id: number | string;
  title: string;
  category: string;
  audioUrl?: string;
  volume: number; // 0 to 1
  isPlaying: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AudioPlayerService {
  private audioElements: Map<string | number, HTMLAudioElement> = new Map();
  private audioCtx: AudioContext | null = null;
  private noiseNodes: Map<string | number, { source: AudioNode; gain: GainNode }> = new Map();

  private activeTracksSubject = new BehaviorSubject<SoundTrack[]>([]);
  public activeTracks$ = this.activeTracksSubject.asObservable();

  private currentMeditationAudio: HTMLAudioElement | null = null;
  private isMeditationPlayingSubject = new BehaviorSubject<boolean>(false);
  public isMeditationPlaying$ = this.isMeditationPlayingSubject.asObservable();

  private meditationProgressSubject = new BehaviorSubject<number>(0);
  public meditationProgress$ = this.meditationProgressSubject.asObservable();

  private getAudioContext(): AudioContext {
    if (!this.audioCtx) {
      const AudioCtxClass = window.AudioContext || (window as any).webkitAudioContext;
      this.audioCtx = new AudioCtxClass();
    }
    if (this.audioCtx.state === 'suspended') {
      this.audioCtx.resume();
    }
    return this.audioCtx;
  }

  // Play or pause relaxation resource sound
  toggleSound(track: SoundTrack): void {
    const isCurrentlyPlaying = track.isPlaying;
    if (isCurrentlyPlaying) {
      this.stopSound(track.id);
    } else {
      this.playSound(track);
    }
  }

  playSound(track: SoundTrack): void {
    const id = track.id;

    // Try HTML5 Audio element first with audioUrl
    if (track.audioUrl) {
      let audio = this.audioElements.get(id);
      if (!audio) {
        audio = new Audio(track.audioUrl);
        audio.loop = true;
        audio.volume = track.volume;
        this.audioElements.set(id, audio);
      }

      audio.play().then(() => {
        this.updateTrackState(track.id, true, track.volume);
      }).catch(err => {
        console.warn('HTML5 audio play blocked/unavailable, falling back to Web Audio soothing sound synthesis', err);
        this.startSynthesizedSound(track);
      });
    } else {
      this.startSynthesizedSound(track);
    }
  }

  stopSound(id: number | string): void {
    const audio = this.audioElements.get(id);
    if (audio) {
      audio.pause();
    }

    const synth = this.noiseNodes.get(id);
    if (synth) {
      synth.gain.gain.linearRampToValueAtTime(0.001, this.getAudioContext().currentTime + 0.3);
      setTimeout(() => {
        try {
          (synth.source as any).stop?.();
          synth.source.disconnect();
        } catch (e) {}
        this.noiseNodes.delete(id);
      }, 350);
    }

    this.updateTrackState(id, false);
  }

  setVolume(id: number | string, volume: number): void {
    const audio = this.audioElements.get(id);
    if (audio) {
      audio.volume = volume;
    }

    const synth = this.noiseNodes.get(id);
    if (synth && this.audioCtx) {
      synth.gain.gain.setValueAtTime(volume * 0.15, this.audioCtx.currentTime);
    }

    this.updateTrackState(id, undefined, volume);
  }

  stopAllSounds(): void {
    this.audioElements.forEach((audio, id) => {
      audio.pause();
    });
    this.noiseNodes.forEach((synth, id) => {
      try {
        (synth.source as any).stop?.();
        synth.source.disconnect();
      } catch (e) {}
    });
    this.noiseNodes.clear();

    const currentList = this.activeTracksSubject.value.map(t => ({ ...t, isPlaying: false }));
    this.activeTracksSubject.next(currentList);
  }

  // Synthesizes soothing ambient sounds using Web Audio API
  private startSynthesizedSound(track: SoundTrack): void {
    try {
      const ctx = this.getAudioContext();
      const gainNode = ctx.createGain();
      gainNode.gain.setValueAtTime(track.volume * 0.12, ctx.currentTime);
      gainNode.connect(ctx.destination);

      if (track.category === 'MEDITATION_MUSIC' || track.category === 'PIANO') {
        // Binaural / resonant calm harmonic oscillator
        const osc = ctx.createOscillator();
        osc.type = 'sine';
        osc.frequency.setValueAtTime(432, ctx.currentTime); // 432 Hz healing tone
        osc.connect(gainNode);
        osc.start();
        this.noiseNodes.set(track.id, { source: osc, gain: gainNode });
      } else {
        // Nature / rain / ocean / forest soothing noise buffer
        const bufferSize = ctx.sampleRate * 2;
        const noiseBuffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
        const output = noiseBuffer.getChannelData(0);
        let b0 = 0, b1 = 0, b2 = 0;
        for (let i = 0; i < bufferSize; i++) {
          const white = Math.random() * 2 - 1;
          b0 = 0.99886 * b0 + white * 0.0555179;
          b1 = 0.99332 * b1 + white * 0.0750759;
          b2 = 0.96900 * b2 + white * 0.1538520;
          output[i] = (b0 + b1 + b2) * 0.11; // Pink noise
        }

        const whiteNoise = ctx.createBufferSource();
        whiteNoise.buffer = noiseBuffer;
        whiteNoise.loop = true;

        // Filter based on nature or rain
        const filter = ctx.createBiquadFilter();
        filter.type = track.category === 'OCEAN' ? 'lowpass' : 'bandpass';
        filter.frequency.setValueAtTime(track.category === 'OCEAN' ? 400 : 800, ctx.currentTime);

        whiteNoise.connect(filter);
        filter.connect(gainNode);
        whiteNoise.start();

        this.noiseNodes.set(track.id, { source: whiteNoise, gain: gainNode });
      }

      this.updateTrackState(track.id, true, track.volume);
    } catch (e) {
      console.error('Audio synthesis failed', e);
    }
  }

  // --- Guided Meditation Player ---
  playMeditationAudio(url: string, onEnded?: () => void): void {
    if (this.currentMeditationAudio) {
      this.currentMeditationAudio.pause();
    }

    this.currentMeditationAudio = new Audio(url);
    this.currentMeditationAudio.ontimeupdate = () => {
      if (this.currentMeditationAudio && this.currentMeditationAudio.duration) {
        const pct = (this.currentMeditationAudio.currentTime / this.currentMeditationAudio.duration) * 100;
        this.meditationProgressSubject.next(pct);
      }
    };

    this.currentMeditationAudio.onended = () => {
      this.isMeditationPlayingSubject.next(false);
      this.meditationProgressSubject.next(100);
      if (onEnded) onEnded();
    };

    this.currentMeditationAudio.play().then(() => {
      this.isMeditationPlayingSubject.next(true);
    }).catch(err => {
      console.warn('Meditation audio play error', err);
      // Fallback timer simulation for completion
      this.isMeditationPlayingSubject.next(true);
    });
  }

  pauseMeditationAudio(): void {
    if (this.currentMeditationAudio) {
      this.currentMeditationAudio.pause();
      this.isMeditationPlayingSubject.next(false);
    }
  }

  resumeMeditationAudio(): void {
    if (this.currentMeditationAudio) {
      this.currentMeditationAudio.play().then(() => {
        this.isMeditationPlayingSubject.next(true);
      });
    }
  }

  stopMeditationAudio(): void {
    if (this.currentMeditationAudio) {
      this.currentMeditationAudio.pause();
      this.currentMeditationAudio.currentTime = 0;
      this.isMeditationPlayingSubject.next(false);
      this.meditationProgressSubject.next(0);
    }
  }

  private updateTrackState(id: number | string, isPlaying?: boolean, volume?: number): void {
    const tracks = this.activeTracksSubject.value;
    const existing = tracks.find(t => t.id === id);
    if (existing) {
      if (isPlaying !== undefined) existing.isPlaying = isPlaying;
      if (volume !== undefined) existing.volume = volume;
      this.activeTracksSubject.next([...tracks]);
    }
  }
}
