import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, ProgressReport, WellnessGoal } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ProgressService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getProgressReport(): Observable<ApiResponse<ProgressReport>> {
    return this.http.get<ApiResponse<ProgressReport>>(`${this.apiUrl}/progress/report`);
  }

  getGoals(): Observable<ApiResponse<WellnessGoal[]>> {
    return this.http.get<ApiResponse<WellnessGoal[]>>(`${this.apiUrl}/goals`);
  }

  createGoal(goal: {
    title: string;
    targetType: string;
    targetValue: number;
    currentValue?: number;
    period: string;
    targetDate: string;
  }): Observable<ApiResponse<WellnessGoal>> {
    return this.http.post<ApiResponse<WellnessGoal>>(`${this.apiUrl}/goals`, goal);
  }

  updateGoalProgress(id: number, amount: number = 1): Observable<ApiResponse<WellnessGoal>> {
    return this.http.patch<ApiResponse<WellnessGoal>>(`${this.apiUrl}/goals/${id}/progress?amount=${amount}`, {});
  }

  deleteGoal(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/goals/${id}`);
  }
}
