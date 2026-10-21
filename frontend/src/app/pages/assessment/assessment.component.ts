import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { StressService } from '../../services/stress.service';
import { StressAssessmentRequest, StressAssessmentResult } from '../../models/models';

@Component({
  selector: 'app-assessment',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './assessment.component.html',
  styleUrls: ['./assessment.component.css']
})
export class AssessmentComponent implements OnInit {
  // Assessment Form State
  sleepRating = 3;
  workloadRating = 3;
  moodRating = 3;
  tensionRating = 3;
  concentrationRating = 3;
  energyRating = 3;
  notes = '';

  isSubmitting = false;
  latestResult: StressAssessmentResult | null = null;
  history: StressAssessmentResult[] = [];
  activeTab: 'questionnaire' | 'history' = 'questionnaire';

  constructor(private stressService: StressService) {}

  ngOnInit(): void {
    this.loadLatestResult();
    this.loadHistory();
  }

  loadLatestResult(): void {
    this.stressService.getLatest().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.latestResult = res.data;
        }
      },
      error: () => {}
    });
  }

  loadHistory(): void {
    this.stressService.getHistory().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.history = res.data;
        }
      },
      error: () => {}
    });
  }

  submitAssessment(): void {
    this.isSubmitting = true;

    const request: StressAssessmentRequest = {
      sleepRating: Number(this.sleepRating),
      workloadRating: Number(this.workloadRating),
      moodRating: Number(this.moodRating),
      tensionRating: Number(this.tensionRating),
      concentrationRating: Number(this.concentrationRating),
      energyRating: Number(this.energyRating),
      notes: this.notes
    };

    this.stressService.submitAssessment(request).subscribe({
      next: (res) => {
        this.isSubmitting = false;
        if (res.success && res.data) {
          this.latestResult = res.data;
          this.loadHistory();
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      },
      error: () => {
        this.isSubmitting = false;
      }
    });
  }

  retakeAssessment(): void {
    this.latestResult = null;
    this.activeTab = 'questionnaire';
    this.sleepRating = 3;
    this.workloadRating = 3;
    this.moodRating = 3;
    this.tensionRating = 3;
    this.concentrationRating = 3;
    this.energyRating = 3;
    this.notes = '';
  }
}
