import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('stressless_token');

  if (token) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

export const adminGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('stressless_token');
  const userStr = localStorage.getItem('stressless_user');

  if (token && userStr) {
    try {
      const user = JSON.parse(userStr);
      if (user.role === 'ROLE_ADMIN') {
        return true;
      }
    } catch (e) {
      // parse error
    }
  }

  router.navigate(['/dashboard']);
  return false;
};
