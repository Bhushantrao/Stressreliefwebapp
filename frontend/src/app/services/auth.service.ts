import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, JwtResponse, User } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  private currentUserSubject = new BehaviorSubject<JwtResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private isDarkModeSubject = new BehaviorSubject<boolean>(false);
  public isDarkMode$ = this.isDarkModeSubject.asObservable();

  constructor(private http: HttpClient) {
    const savedUser = localStorage.getItem('stressless_user');
    if (savedUser) {
      try {
        this.currentUserSubject.next(JSON.parse(savedUser));
      } catch (e) {
        localStorage.removeItem('stressless_user');
      }
    }

    // Initialize Theme
    const savedTheme = localStorage.getItem('stressless_theme');
    const isDark = savedTheme === 'dark';
    this.isDarkModeSubject.next(isDark);
    if (isDark) {
      document.body.setAttribute('data-theme', 'dark');
    }
  }

  login(credentials: { email: string; password: string }): Observable<ApiResponse<JwtResponse>> {
    return this.http.post<ApiResponse<JwtResponse>>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response.success && response.data) {
          localStorage.setItem('stressless_token', response.data.token);
          localStorage.setItem('stressless_user', JSON.stringify(response.data));
          this.currentUserSubject.next(response.data);
        }
      })
    );
  }

  register(userData: { name: string; email: string; password: string; role?: string }): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/register`, userData);
  }

  logout(): void {
    localStorage.removeItem('stressless_token');
    localStorage.removeItem('stressless_user');
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('stressless_token');
  }

  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user ? user.role === 'ROLE_ADMIN' : false;
  }

  getCurrentUser(): JwtResponse | null {
    return this.currentUserSubject.value;
  }

  updateCurrentUserProfile(name: string, avatarUrl?: string, bio?: string): void {
    const current = this.currentUserSubject.value;
    if (current) {
      current.name = name;
      if (avatarUrl) current.avatarUrl = avatarUrl;
      localStorage.setItem('stressless_user', JSON.stringify(current));
      this.currentUserSubject.next({ ...current });
    }
  }

  toggleTheme(): void {
    const current = this.isDarkModeSubject.value;
    const newTheme = !current;
    this.isDarkModeSubject.next(newTheme);
    if (newTheme) {
      document.body.setAttribute('data-theme', 'dark');
      localStorage.setItem('stressless_theme', 'dark');
    } else {
      document.body.removeAttribute('data-theme');
      localStorage.setItem('stressless_theme', 'light');
    }
  }
}
