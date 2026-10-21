import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, MeditationSession } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class MeditationService {
  private apiUrl = `${environment.apiUrl}/meditations`;

  constructor(private http: HttpClient) {}

  getSessions(category?: string): Observable<ApiResponse<MeditationSession[]>> {
    let params = new HttpParams();
    if (category && category !== 'ALL') {
      params = params.set('category', category);
    }
    return this.http.get<ApiResponse<MeditationSession[]>>(this.apiUrl, { params });
  }

  getFeatured(): Observable<ApiResponse<MeditationSession[]>> {
    return this.http.get<ApiResponse<MeditationSession[]>>(`${this.apiUrl}/featured`);
  }

  getSessionById(id: number): Observable<ApiResponse<MeditationSession>> {
    return this.http.get<ApiResponse<MeditationSession>>(`${this.apiUrl}/${id}`);
  }

  recordCompletion(sessionId: number, durationSeconds?: number): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/complete`, {
      meditationSessionId: sessionId,
      durationSeconds
    });
  }

  getHistory(): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.apiUrl}/history`);
  }
}
