import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, RelaxationResource } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class MusicService {
  private apiUrl = `${environment.apiUrl}/music`;

  constructor(private http: HttpClient) {}

  getResources(category?: string): Observable<ApiResponse<RelaxationResource[]>> {
    let params = new HttpParams();
    if (category && category !== 'ALL') {
      params = params.set('category', category);
    }
    return this.http.get<ApiResponse<RelaxationResource[]>>(this.apiUrl, { params });
  }

  getFeatured(): Observable<ApiResponse<RelaxationResource[]>> {
    return this.http.get<ApiResponse<RelaxationResource[]>>(`${this.apiUrl}/featured`);
  }
}
