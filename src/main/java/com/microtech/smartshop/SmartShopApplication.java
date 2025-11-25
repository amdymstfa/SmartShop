package com.microtech.smartshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartShopApplication.class, args);
	}

}
