import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MoodService } from '../../services/mood.service';
import { MoodEntry, MoodTrend } from '../../models/models';

@Component({
  selector: 'app-mood',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mood.component.html',
  styleUrls: ['./mood.component.css']
})
export class MoodComponent implements OnInit {
  // New Mood Form
  selectedMood: 'HAPPY' | 'CALM' | 'NEUTRAL' | 'ANXIOUS' | 'SAD' = 'CALM';
  energyLevel = 3;
  notes = '';
  tags = '';
  isSubmitting = false;
  successNotice = '';

  // Trends & History
  selectedDays = 7;
  trends: MoodTrend | null = null;
  history: MoodEntry[] = [];
  isLoading = true;

  moodOptions: Array<{
    value: 'HAPPY' | 'CALM' | 'NEUTRAL' | 'ANXIOUS' | 'SAD';
    label: string;
    emoji: string;
    color: string;
  }> = [
    { value: 'HAPPY', label: 'Happy', emoji: '😊', color: '#fef08a' },
    { value: 'CALM', label: 'Calm', emoji: '😌', color: '#a7f3d0' },
    { value: 'NEUTRAL', label: 'Neutral', emoji: '😐', color: '#e2e8f0' },
    { value: 'ANXIOUS', label: 'Anxious', emoji: '😰', color: '#fed7aa' },
    { value: 'SAD', label: 'Sad', emoji: '😔', color: '#bae6fd' }
  ];

  selectMood(m: 'HAPPY' | 'CALM' | 'NEUTRAL' | 'ANXIOUS' | 'SAD'): void {
    this.selectedMood = m;
  }

  constructor(private moodService: MoodService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.moodService.getTrends(this.selectedDays).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.trends = res.data;
        }
        this.loadHistory();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  loadHistory(): void {
    this.moodService.getHistory().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.history = res.data;
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  changePeriod(days: number): void {
    this.selectedDays = days;
    this.loadData();
  }

  submitMood(): void {
    this.isSubmitting = true;
    this.successNotice = '';

    this.moodService.logMood({
      mood: this.selectedMood,
      energyLevel: Number(this.energyLevel),
      notes: this.notes.trim(),
      tags: this.tags.trim()
    }).subscribe({
      next: (res) => {
        this.isSubmitting = false;
        if (res.success) {
          this.successNotice = 'Mood logged successfully!';
          this.notes = '';
          this.tags = '';
          this.loadData();
          setTimeout(() => {
            this.successNotice = '';
          }, 3500);
        }
      },
      error: () => {
        this.isSubmitting = false;
      }
    });
  }

  deleteEntry(id?: number): void {
    if (!id) return;
    if (confirm('Delete this mood entry?')) {
      this.moodService.deleteMood(id).subscribe({
        next: () => {
          this.loadData();
        }
      });
    }
  }

  getDistributionTotal(): number {
    if (!this.trends || !this.trends.moodDistribution) return 1;
    const values = Object.values(this.trends.moodDistribution);
    const sum = values.reduce((a, b) => a + b, 0);
    return sum === 0 ? 1 : sum;
  }
}
