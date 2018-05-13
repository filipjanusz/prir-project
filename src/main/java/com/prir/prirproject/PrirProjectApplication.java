package com.prir.prirproject;

import com.prir.prirproject.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PrirProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrirProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return args -> storageService.init();
	}
}
