package com.esprit.microservice.mini_projet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@EnableDiscoveryClient
public class MiniProjetApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniProjetApplication.class, args);
    }

}
