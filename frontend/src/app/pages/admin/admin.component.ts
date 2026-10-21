import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../services/admin.service';
import { MeditationService } from '../../services/meditation.service';
import { MusicService } from '../../services/music.service';
import { AdminStats, MeditationSession, MotivationalQuote, RelaxationResource, User } from '../../models/models';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  stats: AdminStats | null = null;
  users: User[] = [];
  quotes: MotivationalQuote[] = [];
  meditations: MeditationSession[] = [];
  sounds: RelaxationResource[] = [];

  activeTab: 'stats' | 'quotes' | 'meditations' | 'sounds' | 'users' = 'stats';
  isLoading = true;
  feedbackMessage = '';

  // Quote Modal
  isQuoteModalOpen = false;
  editingQuoteId: number | null = null;
  quoteText = '';
  quoteAuthor = '';
  quoteCategory = 'Mindfulness';
  quoteActive = true;

  // Meditation Modal
  isMeditationModalOpen = false;
  editingMedId: number | null = null;
  medTitle = '';
  medDesc = '';
  medCategory = 'Mindfulness';
  medDuration = 5;
  medAudioUrl = '';
  medImageUrl = '';
  medDifficulty: 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED' = 'BEGINNER';
  medInstructions = '';

  // Sound Modal
  isSoundModalOpen = false;
  editingSoundId: number | null = null;
  soundTitle = '';
  soundCategory: 'RAIN' | 'OCEAN' | 'FOREST' | 'NIGHT' | 'WHITE_NOISE' | 'MEDITATION_MUSIC' | 'PIANO' = 'RAIN';
  soundAudioUrl = '';
  soundDesc = '';

  constructor(
    private adminService: AdminService,
    private meditationService: MeditationService,
    private musicService: MusicService
  ) {}

  ngOnInit(): void {
    this.loadStats();
    this.loadQuotes();
    this.loadMeditations();
    this.loadSounds();
    this.loadUsers();
  }

  loadStats(): void {
    this.isLoading = true;
    this.adminService.getStats().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.stats = res.data;
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  loadQuotes(): void {
    this.adminService.getQuotes().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.quotes = res.data;
        }
      }
    });
  }

  loadMeditations(): void {
    this.meditationService.getSessions().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.meditations = res.data;
        }
      }
    });
  }

  loadSounds(): void {
    this.musicService.getResources().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.sounds = res.data;
        }
      }
    });
  }

  loadUsers(): void {
    this.adminService.getUsers().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.users = res.data;
        }
      }
    });
  }

  showFeedback(msg: string): void {
    this.feedbackMessage = msg;
    setTimeout(() => {
      this.feedbackMessage = '';
    }, 4000);
  }

  // --- Quote Operations ---
  openCreateQuote(): void {
    this.editingQuoteId = null;
    this.quoteText = '';
    this.quoteAuthor = '';
    this.quoteCategory = 'Mindfulness';
    this.quoteActive = true;
    this.isQuoteModalOpen = true;
  }

  openEditQuote(q: MotivationalQuote): void {
    if (!q.id) return;
    this.editingQuoteId = q.id;
    this.quoteText = q.quoteText;
    this.quoteAuthor = q.author;
    this.quoteCategory = q.category || 'Mindfulness';
    this.quoteActive = q.active ?? true;
    this.isQuoteModalOpen = true;
  }

  saveQuote(): void {
    if (!this.quoteText.trim()) return;
    const payload: MotivationalQuote = {
      quoteText: this.quoteText.trim(),
      author: this.quoteAuthor.trim() || 'Unknown',
      category: this.quoteCategory,
      active: this.quoteActive
    };

    if (this.editingQuoteId) {
      this.adminService.updateQuote(this.editingQuoteId, payload).subscribe({
        next: () => {
          this.isQuoteModalOpen = false;
          this.showFeedback('Quote updated successfully');
          this.loadQuotes();
          this.loadStats();
        }
      });
    } else {
      this.adminService.createQuote(payload).subscribe({
        next: () => {
          this.isQuoteModalOpen = false;
          this.showFeedback('Quote created successfully');
          this.loadQuotes();
          this.loadStats();
        }
      });
    }
  }

  deleteQuote(id?: number): void {
    if (!id) return;
    if (confirm('Delete this motivational quote?')) {
      this.adminService.deleteQuote(id).subscribe({
        next: () => {
          this.showFeedback('Quote deleted');
          this.loadQuotes();
          this.loadStats();
        }
      });
    }
  }

  // --- Meditation Operations ---
  openCreateMeditation(): void {
    this.editingMedId = null;
    this.medTitle = '';
    this.medDesc = '';
    this.medCategory = 'Mindfulness';
    this.medDuration = 5;
    this.medAudioUrl = '';
    this.medImageUrl = '';
    this.medDifficulty = 'BEGINNER';
    this.medInstructions = '';
    this.isMeditationModalOpen = true;
  }

  openEditMeditation(m: MeditationSession): void {
    if (!m.id) return;
    this.editingMedId = m.id;
    this.medTitle = m.title;
    this.medDesc = m.description;
    this.medCategory = m.category;
    this.medDuration = m.durationMinutes;
    this.medAudioUrl = m.audioUrl || '';
    this.medImageUrl = m.imageUrl || '';
    this.medDifficulty = m.difficulty || 'BEGINNER';
    this.medInstructions = m.instructions || '';
    this.isMeditationModalOpen = true;
  }

  saveMeditation(): void {
    if (!this.medTitle.trim() || !this.medDesc.trim()) return;

    const payload: MeditationSession = {
      title: this.medTitle.trim(),
      description: this.medDesc.trim(),
      category: this.medCategory,
      durationMinutes: Number(this.medDuration),
      audioUrl: this.medAudioUrl.trim(),
      imageUrl: this.medImageUrl.trim(),
      difficulty: this.medDifficulty,
      instructions: this.medInstructions.trim()
    };

    if (this.editingMedId) {
      this.adminService.updateMeditation(this.editingMedId, payload).subscribe({
        next: () => {
          this.isMeditationModalOpen = false;
          this.showFeedback('Meditation session updated');
          this.loadMeditations();
          this.loadStats();
        }
      });
    } else {
      this.adminService.createMeditation(payload).subscribe({
        next: () => {
          this.isMeditationModalOpen = false;
          this.showFeedback('Meditation session created');
          this.loadMeditations();
          this.loadStats();
        }
      });
    }
  }

  deleteMeditation(id?: number): void {
    if (!id) return;
    if (confirm('Delete this guided meditation session?')) {
      this.adminService.deleteMeditation(id).subscribe({
        next: () => {
          this.showFeedback('Meditation session removed');
          this.loadMeditations();
          this.loadStats();
        }
      });
    }
  }

  // --- Sound Operations ---
  openCreateSound(): void {
    this.editingSoundId = null;
    this.soundTitle = '';
    this.soundCategory = 'RAIN';
    this.soundAudioUrl = '';
    this.soundDesc = '';
    this.isSoundModalOpen = true;
  }

  openEditSound(s: RelaxationResource): void {
    if (!s.id) return;
    this.editingSoundId = s.id;
    this.soundTitle = s.title;
    this.soundCategory = s.category;
    this.soundAudioUrl = s.audioUrl;
    this.soundDesc = s.description || '';
    this.isSoundModalOpen = true;
  }

  saveSound(): void {
    if (!this.soundTitle.trim() || !this.soundAudioUrl.trim()) return;

    const payload: RelaxationResource = {
      title: this.soundTitle.trim(),
      category: this.soundCategory,
      audioUrl: this.soundAudioUrl.trim(),
      description: this.soundDesc.trim()
    };

    if (this.editingSoundId) {
      this.adminService.updateSound(this.editingSoundId, payload).subscribe({
        next: () => {
          this.isSoundModalOpen = false;
          this.showFeedback('Sound resource updated');
          this.loadSounds();
          this.loadStats();
        }
      });
    } else {
      this.adminService.createSound(payload).subscribe({
        next: () => {
          this.isSoundModalOpen = false;
          this.showFeedback('Sound resource added');
          this.loadSounds();
          this.loadStats();
        }
      });
    }
  }

  deleteSound(id?: number): void {
    if (!id) return;
    if (confirm('Delete this relaxation sound?')) {
      this.adminService.deleteSound(id).subscribe({
        next: () => {
          this.showFeedback('Sound resource deleted');
          this.loadSounds();
          this.loadStats();
        }
      });
    }
  }
}
