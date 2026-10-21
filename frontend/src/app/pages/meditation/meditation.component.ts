import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MeditationService } from '../../services/meditation.service';
import { AudioPlayerService } from '../../services/audio-player.service';
import { MeditationSession } from '../../models/models';

@Component({
  selector: 'app-meditation',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './meditation.component.html',
  styleUrls: ['./meditation.component.css']
})
export class MeditationComponent implements OnInit {
  sessions: MeditationSession[] = [];
  filteredSessions: MeditationSession[] = [];
  selectedCategory: string = 'ALL';
  categories: string[] = ['ALL', 'Mindfulness', 'Stress', 'Sleep', 'Anxiety', 'Focus'];

  currentSession: MeditationSession | null = null;
  isPlaying = false;
  progressPercent = 0;
  completionNotice = false;
  isLoading = true;

  constructor(
    private meditationService: MeditationService,
    public audioService: AudioPlayerService
  ) {}

  ngOnInit(): void {
    this.loadSessions();

    this.audioService.isMeditationPlaying$.subscribe(playing => {
      this.isPlaying = playing;
    });

    this.audioService.meditationProgress$.subscribe(prog => {
      this.progressPercent = prog;
    });
  }

  loadSessions(): void {
    this.isLoading = true;
    this.meditationService.getSessions().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.sessions = res.data;
          this.filterCategory(this.selectedCategory);
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  filterCategory(cat: string): void {
    this.selectedCategory = cat;
    if (cat === 'ALL') {
      this.filteredSessions = [...this.sessions];
    } else {
      this.filteredSessions = this.sessions.filter(s =>
        s.category.toLowerCase() === cat.toLowerCase()
      );
    }
  }

  playSession(session: MeditationSession): void {
    if (this.currentSession?.id === session.id && this.isPlaying) {
      this.audioService.pauseMeditationAudio();
      return;
    }

    if (this.currentSession?.id === session.id && !this.isPlaying) {
      this.audioService.resumeMeditationAudio();
      return;
    }

    this.currentSession = session;
    const url = session.audioUrl || 'https://cdn.pixabay.com/download/audio/2022/05/27/audio_1808fbf07a.mp3?filename=meditation-mindfulness-ambient-112191.mp3';

    this.audioService.playMeditationAudio(url, () => {
      this.markCompleted(session);
    });
  }

  pauseSession(): void {
    this.audioService.pauseMeditationAudio();
  }

  resumeSession(): void {
    this.audioService.resumeMeditationAudio();
  }

  stopSession(): void {
    this.audioService.stopMeditationAudio();
    this.currentSession = null;
    this.progressPercent = 0;
  }

  markCompleted(session: MeditationSession): void {
    if (!session.id) return;
    this.meditationService.recordCompletion(session.id, session.durationMinutes * 60).subscribe({
      next: () => {
        this.completionNotice = true;
        setTimeout(() => {
          this.completionNotice = false;
        }, 5000);
      },
      error: () => {}
    });
  }
}
