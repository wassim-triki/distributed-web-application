import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { KeycloakService } from '../app/services/keycloak.service';
import { AsyncPipe } from '@angular/common';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "./components/header/header.component"; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, AsyncPipe, HeaderComponent], // Add AsyncPipe
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  private keycloak = inject(KeycloakService);
  
  // Expose authentication state publicly
  isAuthenticated$ = this.keycloak.isAuthenticated();

  constructor() {
    this.initializeKeycloak().catch(error => {
      console.error('Critical authentication error:', error);
    });
  }

  private async initializeKeycloak(): Promise<void> {
    try {
      await this.keycloak.init();
      
      if (!this.keycloak.getKeycloakInstance().authenticated) {
        this.keycloak.login();
      }
    } catch (error) {
      console.error('Authentication initialization failed:', error);
      }
  }
}