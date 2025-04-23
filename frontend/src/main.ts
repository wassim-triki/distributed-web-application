import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
import { KeycloakService } from './app/services/keycloak.service';
import { authInterceptor } from './app/interceptors/auth.interceptor';

async function initializeApp() {
  const keycloakService = new KeycloakService();
  try {
    const authenticated = await keycloakService.init();
    console.log('Keycloak initialization result:', authenticated);
  } catch (error) {
    console.error('Keycloak initialization failed:', error);
    // Allow app to bootstrap with unauthenticated state
  }

  // Merge appConfig with additional providers
  return {
    ...appConfig,
    providers: [
      ...(appConfig.providers || []),
      provideHttpClient(withInterceptors([authInterceptor])),
      provideRouter([]), // Ensure router is provided if not in appConfig
      { provide: KeycloakService, useValue: keycloakService },
    ],
  };
}

initializeApp()
  .then((config) => {
    bootstrapApplication(AppComponent, config).catch((err) =>
      console.error('Bootstrap failed:', err)
    );
  })
  .catch((err) => console.error('App initialization failed:', err));