package tn.esprit.microservice.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("reclamation_service", r -> r.path("/reclamations/**")
                        .uri("lb://reclamation-service"))  // The exact service name in Eureka
                .route("stock_service", r -> r.path("/stocks/**")
                        .uri("lb://stock-service"))  // The exact service name in Eureka
                .route("ORDRE-SERVICE", r -> r.path("/orders/**")
                        .uri("lb://ORDRE-SERVICE"))  // The exact service name in Eureka

                // Second route for order lines
                .route("ORDER-LINES", r -> r.path("/orders-line/**")
                        .uri("lb://ORDRE-SERVICE"))
                .route("product-service", r -> r.path("/products/**")
                        .uri("http://localhost:8081")) // ou "lb://PRODUCT-SERVICE" si Eureka
                .build();// The exact service name in Eureka


    }
}
