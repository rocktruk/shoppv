package com.online.mall.shoppv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableAsync
@EnableJpaAuditing
public class ShoppingApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
}
