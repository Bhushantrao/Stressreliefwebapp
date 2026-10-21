import { Routes } from '@angular/router';
import { LandingComponent } from './pages/landing/landing.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AssessmentComponent } from './pages/assessment/assessment.component';
import { BreathingComponent } from './pages/breathing/breathing.component';
import { MeditationComponent } from './pages/meditation/meditation.component';
import { MusicComponent } from './pages/music/music.component';
import { MoodComponent } from './pages/mood/mood.component';
import { JournalComponent } from './pages/journal/journal.component';
import { ProgressComponent } from './pages/progress/progress.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { AdminComponent } from './pages/admin/admin.component';
import { authGuard, adminGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: LandingComponent, title: 'StressLess – Stress Relief & Mental Wellness' },
  { path: 'home', redirectTo: '', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, title: 'Sign In – StressLess' },
  { path: 'register', component: RegisterComponent, title: 'Create Account – StressLess' },

  // Protected Member Routes
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard], title: 'Dashboard – StressLess' },
  { path: 'assessment', component: AssessmentComponent, canActivate: [authGuard], title: 'Stress Assessment – StressLess' },
  { path: 'breathing', component: BreathingComponent, canActivate: [authGuard], title: 'Breathing Exercises – StressLess' },
  { path: 'meditation', component: MeditationComponent, canActivate: [authGuard], title: 'Guided Meditation – StressLess' },
  { path: 'music', component: MusicComponent, canActivate: [authGuard], title: 'Relaxing Music & Sounds – StressLess' },
  { path: 'mood', component: MoodComponent, canActivate: [authGuard], title: 'Daily Mood Tracker – StressLess' },
  { path: 'journal', component: JournalComponent, canActivate: [authGuard], title: 'Personal Journal – StressLess' },
  { path: 'progress', component: ProgressComponent, canActivate: [authGuard], title: 'Wellness Habits & Progress – StressLess' },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard], title: 'User Profile – StressLess' },

  // Protected Admin Routes
  { path: 'admin', component: AdminComponent, canActivate: [adminGuard], title: 'Admin Management – StressLess' },

  // Fallback
  { path: '**', redirectTo: '' }
];
