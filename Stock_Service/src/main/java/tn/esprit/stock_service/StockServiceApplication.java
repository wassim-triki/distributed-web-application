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
						new Stock(01, 10, 100, "Nabeul", StockStatus.AVAILABLE, "High-resolution screen"),
						new Stock(02, 5, 50, "Tunis", StockStatus.AVAILABLE, "Auto-recharge"),
						new Stock(03, 5, 20, "Bizerte", StockStatus.AVAILABLE, "Smartphone,128GB,Black"),
						new Stock(04, 10, 150, "Béjà", StockStatus.RESERVED, "Laptop,Intel i7,16GB RAM"),
						new Stock(05, 5, 80, "Tabarka", StockStatus.OUT_OF_STOCK, "Wireless Headphones"),
						new Stock(05, 5, 90, "Zarzis", StockStatus.RESERVED, "Gaming Console,1TB HDD"),
						new Stock(06, 10, 350, "Zaghouan", StockStatus.AVAILABLE, "4K LED TV,55-inch")
				));
			}

			stockRepository.findAll().forEach(System.out::println);
		};
	}

}

