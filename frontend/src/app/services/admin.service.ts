import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AdminStats, ApiResponse, MeditationSession, MotivationalQuote, RelaxationResource, User } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  getStats(): Observable<ApiResponse<AdminStats>> {
    return this.http.get<ApiResponse<AdminStats>>(`${this.apiUrl}/stats`);
  }

  getUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users`);
  }

  // Quotes
  getQuotes(): Observable<ApiResponse<MotivationalQuote[]>> {
    return this.http.get<ApiResponse<MotivationalQuote[]>>(`${this.apiUrl}/quotes`);
  }

  createQuote(quote: MotivationalQuote): Observable<ApiResponse<MotivationalQuote>> {
    return this.http.post<ApiResponse<MotivationalQuote>>(`${this.apiUrl}/quotes`, quote);
  }

  updateQuote(id: number, quote: MotivationalQuote): Observable<ApiResponse<MotivationalQuote>> {
    return this.http.put<ApiResponse<MotivationalQuote>>(`${this.apiUrl}/quotes/${id}`, quote);
  }

  deleteQuote(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/quotes/${id}`);
  }

  // Meditations
  createMeditation(session: MeditationSession): Observable<ApiResponse<MeditationSession>> {
    return this.http.post<ApiResponse<MeditationSession>>(`${this.apiUrl}/meditations`, session);
  }

  updateMeditation(id: number, session: MeditationSession): Observable<ApiResponse<MeditationSession>> {
    return this.http.put<ApiResponse<MeditationSession>>(`${this.apiUrl}/meditations/${id}`, session);
  }

  deleteMeditation(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/meditations/${id}`);
  }

  // Relaxation Sounds
  createSound(resource: RelaxationResource): Observable<ApiResponse<RelaxationResource>> {
    return this.http.post<ApiResponse<RelaxationResource>>(`${this.apiUrl}/music`, resource);
  }

  updateSound(id: number, resource: RelaxationResource): Observable<ApiResponse<RelaxationResource>> {
    return this.http.put<ApiResponse<RelaxationResource>>(`${this.apiUrl}/music/${id}`, resource);
  }

  deleteSound(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/music/${id}`);
  }
}
