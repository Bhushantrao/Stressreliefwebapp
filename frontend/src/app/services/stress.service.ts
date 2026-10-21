import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, StressAssessmentRequest, StressAssessmentResult } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class StressService {
  private apiUrl = `${environment.apiUrl}/assessment`;

  constructor(private http: HttpClient) {}

  submitAssessment(data: StressAssessmentRequest): Observable<ApiResponse<StressAssessmentResult>> {
    return this.http.post<ApiResponse<StressAssessmentResult>>(this.apiUrl, data);
  }

  getHistory(): Observable<ApiResponse<StressAssessmentResult[]>> {
    return this.http.get<ApiResponse<StressAssessmentResult[]>>(`${this.apiUrl}/history`);
  }

  getLatest(): Observable<ApiResponse<StressAssessmentResult>> {
    return this.http.get<ApiResponse<StressAssessmentResult>>(`${this.apiUrl}/latest`);
  }
}
