import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { MotivationalQuote } from '../../models/models';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  isLoggedIn = false;
  dailyQuote: MotivationalQuote = {
    quoteText: 'Within you, there is a stillness and a sanctuary to which you can retreat at any time.',
    author: 'Hermann Hesse',
    category: 'Peace'
  };

  constructor(public authService: AuthService, private http: HttpClient) {}

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isAuthenticated();

    // Fetch live daily quote from public backend endpoint
    this.http.get<any>(`${environment.apiUrl}/quotes/daily`).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.dailyQuote = res.data;
        }
      },
      error: () => {}
    });
  }
}
