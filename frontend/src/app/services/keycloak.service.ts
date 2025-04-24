import { Injectable } from '@angular/core';
import Keycloak, { KeycloakInstance } from 'keycloak-js';
import { BehaviorSubject, Observable, catchError, from, of, tap } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  private keycloak!: KeycloakInstance;
  private authStatus = new BehaviorSubject<boolean>(false);
  private tokenRefreshInterval?: number;

  async init(): Promise<void> {
    this.keycloak = new Keycloak({
      url: environment.keycloak.url,
      realm: environment.keycloak.realm,
      clientId: environment.keycloak.clientId,
    });

    try {
      const authenticated = await this.keycloak.init({
        onLoad: 'check-sso',
        checkLoginIframe: false,
        silentCheckSsoRedirectUri: `${window.location.origin}/assets/silent-check-sso.html`,
        pkceMethod: 'S256', // Ensure PKCE is enabled
      });

      console.log('Keycloak initialized successfully:', authenticated);
      this.authStatus.next(authenticated);

      if (!authenticated) {
        this.login();
      } else {
        this.setupTokenRefresh();
      }
    } catch (error: any) {
      console.error('Keycloak initialization failed:', error.message || error);
      this.authStatus.next(false);
      throw error;
    }
  }

  private setupTokenRefresh(): void {
    this.tokenRefreshInterval = window.setInterval(async () => {
      try {
        const refreshed = await this.keycloak.updateToken(70); // Refresh if token expires in 70 seconds
        if (refreshed) {
          console.log('Token refreshed successfully');
        }
      } catch (error) {
        console.error('Token refresh failed:', error);
        this.logout(); // Redirect to login after token refresh failure
      }
    }, 60000); // Check every minute
  }

  login(): void {
    this.keycloak.login({
      redirectUri: window.location.origin,
      prompt: 'login',
    });
  }

  logout(): void {
    this.keycloak.logout({
      redirectUri: window.location.origin,
    });
    if (this.tokenRefreshInterval) {
      window.clearInterval(this.tokenRefreshInterval);
    }
    this.authStatus.next(false);
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  isAuthenticated(): Observable<boolean> {
    return this.authStatus.asObservable();
  }

  getKeycloakInstance(): KeycloakInstance {
    return this.keycloak;
  }

  checkAuthStatus(): Observable<boolean> {
    return from(this.keycloak.updateToken(-1)).pipe(
      tap((authenticated) => this.authStatus.next(authenticated)),
      catchError((error) => {
        console.error('Auth check failed:', error);
        this.authStatus.next(false);
        return of(false); // Return `false` as a fallback
      })
    );
  }
}