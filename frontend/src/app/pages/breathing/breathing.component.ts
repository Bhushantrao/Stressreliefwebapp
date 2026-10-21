import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BreathingService } from '../../services/breathing.service';
import { BreathingSession } from '../../models/models';

type BreathingPhase = 'INHALE' | 'HOLD' | 'EXHALE' | 'REST' | 'READY';

interface TechniqueConfig {
  name: string;
  technique: 'BOX_BREATHING' | 'SLOW_PACED' | 'FOUR_SEVEN_EIGHT';
  description: string;
  inhale: number;
  hold1: number;
  exhale: number;
  hold2: number;
}

@Component({
  selector: 'app-breathing',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './breathing.component.html',
  styleUrls: ['./breathing.component.css']
})
export class BreathingComponent implements OnInit, OnDestroy {
  // Techniques Configuration
  techniques: TechniqueConfig[] = [
    {
      name: 'Box Breathing',
      technique: 'BOX_BREATHING',
      description: 'Equal 4-second phases for acute stress relief, mental discipline, and rapid emotional composure.',
      inhale: 4,
      hold1: 4,
      exhale: 4,
      hold2: 4
    },
    {
      name: 'Slow-Paced Breathing',
      technique: 'SLOW_PACED',
      description: 'Extended 6-second exhalations to stimulate the vagus nerve and promote restorative calm.',
      inhale: 4,
      hold1: 0,
      exhale: 6,
      hold2: 0
    },
    {
      name: '4-7-8 Relaxing Breath',
      technique: 'FOUR_SEVEN_EIGHT',
      description: 'Dr. Andrew Weil’s classic natural tranquilizer for sleep onset, panic relief, and deep unwind.',
      inhale: 4,
      hold1: 7,
      exhale: 8,
      hold2: 0
    }
  ];

  selectedTechnique: TechniqueConfig = this.techniques[0];
  selectedDurationMinutes: number = 3; // 1, 2, 3, 5, 10
  Math = Math;

  // Session State
  state: 'IDLE' | 'RUNNING' | 'PAUSED' | 'COMPLETED' = 'IDLE';
  currentPhase: BreathingPhase = 'READY';
  phaseSecondsLeft: number = 0;
  totalSecondsElapsed: number = 0;
  cyclesCompleted: number = 0;

  // Visual animation scale
  scaleRatio: number = 1.0;

  private timerInterval: any = null;
  private audioCtx: AudioContext | null = null;

  // History
  history: BreathingSession[] = [];
  savedSessionNotice = false;

  constructor(private breathingService: BreathingService) {}

  ngOnInit(): void {
    this.loadHistory();
  }

  ngOnDestroy(): void {
    this.stopSession();
  }

  loadHistory(): void {
    this.breathingService.getHistory().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.history = res.data;
        }
      },
      error: () => {}
    });
  }

  selectTechnique(tech: TechniqueConfig): void {
    if (this.state === 'RUNNING' || this.state === 'PAUSED') {
      if (!confirm('Changing technique will reset your current exercise. Proceed?')) {
        return;
      }
      this.stopSession();
    }
    this.selectedTechnique = tech;
  }

  startSession(): void {
    this.state = 'RUNNING';
    this.totalSecondsElapsed = 0;
    this.cyclesCompleted = 0;
    this.startInhalePhase();
    this.initTimer();
  }

  pauseSession(): void {
    this.state = 'PAUSED';
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }
  }

  resumeSession(): void {
    this.state = 'RUNNING';
    this.initTimer();
  }

  stopSession(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }

    if (this.state === 'RUNNING' || this.state === 'PAUSED') {
      if (this.totalSecondsElapsed >= 20) {
        this.saveCompletedSession();
      }
    }

    this.state = 'IDLE';
    this.currentPhase = 'READY';
    this.phaseSecondsLeft = 0;
    this.scaleRatio = 1.0;
  }

  private initTimer(): void {
    if (this.timerInterval) clearInterval(this.timerInterval);

    this.timerInterval = setInterval(() => {
      this.totalSecondsElapsed++;

      // Check for duration completion
      if (this.totalSecondsElapsed >= this.selectedDurationMinutes * 60) {
        this.finishSession();
        return;
      }

      this.phaseSecondsLeft--;
      if (this.phaseSecondsLeft <= 0) {
        this.advancePhase();
      }
    }, 1000);
  }

  private advancePhase(): void {
    if (this.currentPhase === 'INHALE') {
      if (this.selectedTechnique.hold1 > 0) {
        this.startHoldPhase('HOLD');
      } else {
        this.startExhalePhase();
      }
    } else if (this.currentPhase === 'HOLD') {
      this.startExhalePhase();
    } else if (this.currentPhase === 'EXHALE') {
      if (this.selectedTechnique.hold2 > 0) {
        this.startHoldPhase('REST');
      } else {
        this.cyclesCompleted++;
        this.startInhalePhase();
      }
    } else if (this.currentPhase === 'REST') {
      this.cyclesCompleted++;
      this.startInhalePhase();
    }
  }

  private startInhalePhase(): void {
    this.currentPhase = 'INHALE';
    this.phaseSecondsLeft = this.selectedTechnique.inhale;
    this.scaleRatio = 1.45;
    this.playTone(330, 0.4); // E4 gentle bell
  }

  private startHoldPhase(type: 'HOLD' | 'REST'): void {
    this.currentPhase = type;
    this.phaseSecondsLeft = type === 'HOLD' ? this.selectedTechnique.hold1 : this.selectedTechnique.hold2;
    // Keep scale steady during hold
    this.playTone(392, 0.2); // G4 gentle touch
  }

  private startExhalePhase(): void {
    this.currentPhase = 'EXHALE';
    this.phaseSecondsLeft = this.selectedTechnique.exhale;
    this.scaleRatio = 0.95;
    this.playTone(261.6, 0.5); // C4 grounding tone
  }

  private finishSession(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }
    this.state = 'COMPLETED';
    this.currentPhase = 'READY';
    this.scaleRatio = 1.0;
    this.playCompletionChime();
    this.saveCompletedSession();
  }

  private saveCompletedSession(): void {
    this.breathingService.logSession({
      technique: this.selectedTechnique.technique,
      durationSeconds: this.totalSecondsElapsed,
      cyclesCompleted: Math.max(1, this.cyclesCompleted)
    }).subscribe({
      next: () => {
        this.savedSessionNotice = true;
        this.loadHistory();
        setTimeout(() => {
          this.savedSessionNotice = false;
        }, 4000);
      },
      error: () => {}
    });
  }

  // Soft harmonic acoustic cue using Web Audio API
  private playTone(freq: number, duration: number): void {
    try {
      if (!this.audioCtx) {
        const AudioCtxClass = window.AudioContext || (window as any).webkitAudioContext;
        this.audioCtx = new AudioCtxClass();
      }
      if (this.audioCtx.state === 'suspended') {
        this.audioCtx.resume();
      }
      const osc = this.audioCtx.createOscillator();
      const gain = this.audioCtx.createGain();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(freq, this.audioCtx.currentTime);

      gain.gain.setValueAtTime(0.08, this.audioCtx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.0001, this.audioCtx.currentTime + duration);

      osc.connect(gain);
      gain.connect(this.audioCtx.destination);
      osc.start();
      osc.stop(this.audioCtx.currentTime + duration);
    } catch (e) {
      // Audio context might be restricted before user gesture
    }
  }

  private playCompletionChime(): void {
    setTimeout(() => this.playTone(523.25, 0.8), 0);
    setTimeout(() => this.playTone(659.25, 0.8), 250);
    setTimeout(() => this.playTone(783.99, 1.2), 500);
  }

  formatTime(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }
}
