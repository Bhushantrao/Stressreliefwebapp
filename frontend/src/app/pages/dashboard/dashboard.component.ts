import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { MoodService } from '../../services/mood.service';
import { DashboardStats } from '../../models/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  isLoading = true;
  errorMessage = '';

  // Quick mood logging from dashboard
  selectedMood: string = '';
  isLoggingMood = false;
  moodLoggedSuccess = false;

  constructor(
    private dashboardService: DashboardService,
    private moodService: MoodService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.dashboardService.getStats().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.stats = res.data;
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Unable to load dashboard data. Please try again.';
      }
    });
  }

  quickLogMood(mood: 'HAPPY' | 'CALM' | 'NEUTRAL' | 'ANXIOUS' | 'SAD'): void {
    this.isLoggingMood = true;
    this.moodService.logMood({
      mood,
      energyLevel: mood === 'HAPPY' ? 4 : mood === 'CALM' ? 5 : mood === 'NEUTRAL' ? 3 : mood === 'ANXIOUS' ? 2 : 1,
      notes: 'Quick dashboard check-in'
    }).subscribe({
      next: () => {
        this.isLoggingMood = false;
        this.moodLoggedSuccess = true;
        this.loadDashboardData();
        setTimeout(() => {
          this.moodLoggedSuccess = false;
        }, 3000);
      },
      error: () => {
        this.isLoggingMood = false;
      }
    });
  }
}
