import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserEmail: string | null = null;

  constructor() {
    if (typeof sessionStorage !== 'undefined') {
      this.currentUserEmail = sessionStorage.getItem('currentUserEmail');
    }
  }

  login(email: string) {
    this.currentUserEmail = email;
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.setItem('currentUserEmail', email);
    }
  }

  logout() {
    this.currentUserEmail = null;
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.removeItem('currentUserEmail');
    }
  }

  getCurrentUser(): string | null {
    return this.currentUserEmail;
  }
}