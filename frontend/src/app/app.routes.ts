// src/app/app.routes.ts

import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { EventFormComponent } from './event-form/event-form.component';
import { RegisterComponent } from './register/register.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  // Esta línea es crucial. Redirige la ruta vacía (/) a la ruta 'login'.
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'event-form', 
    component: EventFormComponent,
    canActivate: [authGuard] 
  },
  { path: '**', redirectTo: 'login' }
];