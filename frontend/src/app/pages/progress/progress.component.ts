import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProgressService } from '../../services/progress.service';
import { ProgressReport, WellnessGoal } from '../../models/models';

@Component({
  selector: 'app-progress',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './progress.component.html',
  styleUrls: ['./progress.component.css']
})
export class ProgressComponent implements OnInit {
  report: ProgressReport | null = null;
  isLoading = true;

  // New Goal Modal State
  isGoalModalOpen = false;
  goalTitle = '';
  goalTargetType: 'BREATHING_MINUTES' | 'MEDITATION_MINUTES' | 'MOOD_CHECKINS' | 'JOURNAL_ENTRIES' = 'BREATHING_MINUTES';
  goalTargetValue = 10;
  goalPeriod: 'DAILY' | 'WEEKLY' = 'DAILY';
  goalTargetDate = new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0];
  isSavingGoal = false;
  goalError = '';

  constructor(private progressService: ProgressService) {}

  ngOnInit(): void {
    this.loadProgress();
  }

  loadProgress(): void {
    this.isLoading = true;
    this.progressService.getProgressReport().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.report = res.data;
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  openGoalModal(): void {
    this.goalTitle = '';
    this.goalTargetType = 'BREATHING_MINUTES';
    this.goalTargetValue = 15;
    this.goalPeriod = 'DAILY';
    this.goalTargetDate = new Date(Date.now() + 7 * 86400000).toISOString().split('T')[0];
    this.goalError = '';
    this.isGoalModalOpen = true;
  }

  closeGoalModal(): void {
    this.isGoalModalOpen = false;
  }

  saveGoal(): void {
    if (!this.goalTitle.trim()) {
      this.goalError = 'Please provide a goal title';
      return;
    }

    this.isSavingGoal = true;
    this.goalError = '';

    this.progressService.createGoal({
      title: this.goalTitle.trim(),
      targetType: this.goalTargetType,
      targetValue: Number(this.goalTargetValue),
      period: this.goalPeriod,
      targetDate: this.goalTargetDate
    }).subscribe({
      next: (res) => {
        this.isSavingGoal = false;
        if (res.success) {
          this.closeGoalModal();
          this.loadProgress();
        }
      },
      error: (err) => {
        this.isSavingGoal = false;
        this.goalError = err.error?.message || 'Failed to create goal';
      }
    });
  }

  incrementProgress(goal: WellnessGoal, amount: number = 1): void {
    if (!goal.id) return;
    this.progressService.updateGoalProgress(goal.id, amount).subscribe({
      next: () => {
        this.loadProgress();
      }
    });
  }

  deleteGoal(id?: number): void {
    if (!id) return;
    if (confirm('Delete this wellness goal?')) {
      this.progressService.deleteGoal(id).subscribe({
        next: () => {
          this.loadProgress();
        }
      });
    }
  }

  getMaxBarHeight(): number {
    if (!this.report?.weeklyActivityChart?.length) return 30;
    const max = Math.max(...this.report.weeklyActivityChart.map(d => Math.max(d.breathingMins, d.meditationMins)));
    return max > 0 ? max : 30;
  }
}
