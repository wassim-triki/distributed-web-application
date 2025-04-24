export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8093', // Spring Cloud Gateway URL for development
  keycloak: {
    url: 'http://localhost:9098', // Keycloak server URL
    realm: 'micro-services', // Keycloak realm
    clientId: 'micro-services-angular', // Keycloak client ID
 
  },
};