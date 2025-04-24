import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
import { KeycloakService } from './app/services/keycloak.service';
import { authInterceptor } from './app/interceptors/auth.interceptor';
import { routes as appRoutes } from './app/app.routes'; // Import your application routes

async function initializeApp() {
  const keycloakService = new KeycloakService();
  try {
    const authenticated = await keycloakService.init();
    console.log('Keycloak initialization result:', authenticated);
  } catch (error) {
    console.error('Keycloak initialization failed:', error);
  }

  return {
    ...appConfig,
    providers: [
      ...(appConfig.providers || []),
      provideHttpClient(withInterceptors([authInterceptor])),
      provideRouter(appRoutes), // Use the correct routes here
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