package tn.esprit.microservice.customer;


import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerApplication {

    private final CustomerRepository customerRepository;

    public CustomerApplication(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    @Bean
    ApplicationRunner init() {
        return args -> {
            if (customerRepository.count() == 0) {
                customerRepository.saveAll(List.of(
                        new Customer("Charlie", "Alice", "alice@example.com", "123456789",  "uk"),
                        new Customer("Alice", "Bob", "bob@example.com", "987654321", "tn"),
                        new Customer("Bob", "Charlie", "charlie@example.com", "456789123",  "uk")
                ));
            }

            customerRepository.findAll().forEach(System.out::println);
        };
    }
}
