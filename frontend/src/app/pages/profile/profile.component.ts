import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProfileService } from '../../services/profile.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/models';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  name = '';
  bio = '';
  avatarUrl = '';
  oldPassword = '';
  newPassword = '';

  isSaving = false;
  successMessage = '';
  errorMessage = '';
  isLoading = true;

  avatarOptions = [
    'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=200&q=80',
    'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=200&q=80',
    'https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?auto=format&fit=crop&w=200&q=80',
    'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80',
    'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80'
  ];

  constructor(
    private profileService: ProfileService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.isLoading = true;
    this.profileService.getProfile().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.user = res.data;
          this.name = this.user.name;
          this.bio = this.user.bio || '';
          this.avatarUrl = this.user.avatarUrl || this.avatarOptions[0];
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  selectAvatar(url: string): void {
    this.avatarUrl = url;
  }

  onSubmit(): void {
    if (!this.name.trim()) {
      this.errorMessage = 'Name cannot be empty';
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const payload: any = {
      name: this.name.trim(),
      bio: this.bio.trim(),
      avatarUrl: this.avatarUrl
    };

    if (this.newPassword) {
      if (!this.oldPassword) {
        this.isSaving = false;
        this.errorMessage = 'Please provide your current password to set a new password';
        return;
      }
      payload.oldPassword = this.oldPassword;
      payload.newPassword = this.newPassword;
    }

    this.profileService.updateProfile(payload).subscribe({
      next: (res) => {
        this.isSaving = false;
        if (res.success && res.data) {
          this.user = res.data;
          this.authService.updateCurrentUserProfile(res.data.name, res.data.avatarUrl, res.data.bio);
          this.successMessage = 'Profile updated successfully!';
          this.oldPassword = '';
          this.newPassword = '';
          setTimeout(() => {
            this.successMessage = '';
          }, 4000);
        }
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.message || 'Failed to update profile';
      }
    });
  }
}
