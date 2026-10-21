import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MusicService } from '../../services/music.service';
import { AudioPlayerService, SoundTrack } from '../../services/audio-player.service';
import { RelaxationResource } from '../../models/models';

@Component({
  selector: 'app-music',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.css']
})
export class MusicComponent implements OnInit, OnDestroy {
  tracks: SoundTrack[] = [];
  resources: RelaxationResource[] = [];
  selectedCategory: string = 'ALL';
  categories: string[] = ['ALL', 'RAIN', 'OCEAN', 'FOREST', 'NIGHT', 'PIANO', 'MEDITATION_MUSIC'];
  isLoading = true;
  Math = Math;

  constructor(
    private musicService: MusicService,
    public audioService: AudioPlayerService
  ) {}

  ngOnInit(): void {
    this.loadResources();
  }

  ngOnDestroy(): void {
    // Keep ambient sound playing if desired, or stop on destroy
  }

  loadResources(): void {
    this.isLoading = true;
    this.musicService.getResources().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.resources = res.data;
          this.tracks = this.resources.map(r => ({
            id: r.id || r.title,
            title: r.title,
            category: r.category,
            audioUrl: r.audioUrl,
            volume: 0.7,
            isPlaying: false
          }));
        }
      },
      error: () => {
        this.isLoading = false;
        // Fallback default tracks
        this.tracks = [
          { id: 1, title: 'Gentle Summer Rain', category: 'RAIN', volume: 0.7, isPlaying: false, audioUrl: 'https://cdn.pixabay.com/download/audio/2022/05/16/audio_db65955613.mp3?filename=soft-rain-ambient-111154.mp3' },
          { id: 2, title: 'Pacific Ocean Waves', category: 'OCEAN', volume: 0.7, isPlaying: false, audioUrl: 'https://cdn.pixabay.com/download/audio/2021/09/06/audio_73bb1b590e.mp3?filename=ocean-waves-112906.mp3' },
          { id: 3, title: 'Deep Pine Forest Birds', category: 'FOREST', volume: 0.7, isPlaying: false, audioUrl: 'https://cdn.pixabay.com/download/audio/2022/04/27/audio_4ebca8e89f.mp3?filename=forest-birds-chirping-nature-sound-110756.mp3' },
          { id: 4, title: 'Midnight Campfire & Crickets', category: 'NIGHT', volume: 0.7, isPlaying: false, audioUrl: 'https://cdn.pixabay.com/download/audio/2022/03/10/audio_51c6b2ee5d.mp3?filename=campfire-crackling-fireplace-sound-119594.mp3' },
          { id: 5, title: 'Calm Piano & Ambient Pads', category: 'PIANO', volume: 0.7, isPlaying: false, audioUrl: 'https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3?filename=relaxing-light-piano-10775.mp3' },
          { id: 6, title: '432 Hz Binaural Healing Drone', category: 'MEDITATION_MUSIC', volume: 0.7, isPlaying: false, audioUrl: 'https://cdn.pixabay.com/download/audio/2022/10/14/audio_993952fce6.mp3?filename=tibetan-singing-bowl-meditation-122416.mp3' }
        ];
      }
    });
  }

  toggleTrack(track: SoundTrack): void {
    track.isPlaying = !track.isPlaying;
    if (track.isPlaying) {
      this.audioService.playSound(track);
    } else {
      this.audioService.stopSound(track.id);
    }
  }

  onVolumeChange(track: SoundTrack): void {
    this.audioService.setVolume(track.id, track.volume);
  }

  stopAll(): void {
    this.audioService.stopAllSounds();
    this.tracks.forEach(t => t.isPlaying = false);
  }

  get activeTracksCount(): number {
    return this.tracks.filter(t => t.isPlaying).length;
  }
}
