import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, BreathingSession } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class BreathingService {
  private apiUrl = `${environment.apiUrl}/breathing`;

  constructor(private http: HttpClient) {}

  logSession(session: { technique: string; durationSeconds: number; cyclesCompleted: number }): Observable<ApiResponse<BreathingSession>> {
    return this.http.post<ApiResponse<BreathingSession>>(`${this.apiUrl}/session`, session);
  }

  getHistory(): Observable<ApiResponse<BreathingSession[]>> {
    return this.http.get<ApiResponse<BreathingSession[]>>(`${this.apiUrl}/history`);
  }
}
