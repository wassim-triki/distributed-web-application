package tn.esprit.stock_service;


import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class StockServiceApplication {

	private final StockRepository stockRepository;

	public StockServiceApplication(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(StockServiceApplication.class, args);
	}

	@Bean
	ApplicationRunner init() {
		return args -> {
			if (stockRepository.count() == 0) {
				// Example product IDs 1 and 2 (adjust based on your actual product IDs)
				stockRepository.saveAll(List.of(
						new Stock(1, 100, "Main Warehouse - Shelf 1", StockStatus.AVAILABLE, "Product in good condition"),
						new Stock(2, 0, "Main Warehouse - Shelf 2", StockStatus.OUT_OF_STOCK, "Product is out of stock")
				));
			}

			stockRepository.findAll().forEach(System.out::println);
		};
	}

}

