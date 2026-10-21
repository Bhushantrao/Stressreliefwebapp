import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, MoodEntry, MoodTrend } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class MoodService {
  private apiUrl = `${environment.apiUrl}/mood`;

  constructor(private http: HttpClient) {}

  logMood(entry: { mood: string; energyLevel: number; notes?: string; tags?: string }): Observable<ApiResponse<MoodEntry>> {
    return this.http.post<ApiResponse<MoodEntry>>(this.apiUrl, entry);
  }

  getHistory(): Observable<ApiResponse<MoodEntry[]>> {
    return this.http.get<ApiResponse<MoodEntry[]>>(`${this.apiUrl}/history`);
  }

  getTrends(days: number = 7): Observable<ApiResponse<MoodTrend>> {
    const params = new HttpParams().set('days', days.toString());
    return this.http.get<ApiResponse<MoodTrend>>(`${this.apiUrl}/trends`, { params });
  }

  deleteMood(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/${id}`);
  }
}
