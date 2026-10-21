import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, JournalEntry } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class JournalService {
  private apiUrl = `${environment.apiUrl}/journal`;

  constructor(private http: HttpClient) {}

  getJournals(search?: string): Observable<ApiResponse<JournalEntry[]>> {
    let params = new HttpParams();
    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    return this.http.get<ApiResponse<JournalEntry[]>>(this.apiUrl, { params });
  }

  getJournalById(id: number): Observable<ApiResponse<JournalEntry>> {
    return this.http.get<ApiResponse<JournalEntry>>(`${this.apiUrl}/${id}`);
  }

  createJournal(entry: { title: string; content: string; moodTag?: string; tags?: string }): Observable<ApiResponse<JournalEntry>> {
    return this.http.post<ApiResponse<JournalEntry>>(this.apiUrl, entry);
  }

  updateJournal(id: number, entry: { title: string; content: string; moodTag?: string; tags?: string }): Observable<ApiResponse<JournalEntry>> {
    return this.http.put<ApiResponse<JournalEntry>>(`${this.apiUrl}/${id}`, entry);
  }

  deleteJournal(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/${id}`);
  }
}
