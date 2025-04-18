package tn.esprit.microservice.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient


public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("reclamation_service", r -> r.path("/reclamations/**") // Adjusted path
                        .uri("lb://reclamation-service")) // The exact service name in Eureka
                .route("stock_service", r -> r.path("/stocks/**") // Adjusted path
                        .uri("lb://stock-service")).route("ORDRE-SERVICE", r -> r.path("/orders/**").uri("lb://ORDRE-SERVICE"))

                        // Deuxième route pour les lignes de commande
                        .route("ORDER-LINES", r -> r.path("/orders-line/**").uri("lb://ORDRE-SERVICE")) // The exact service name in Eureka
                .route("product_service", r -> r.path("/products/**")
                        .uri("lb://PRODUCT_SERVICE"))

                .build();
    }



}
