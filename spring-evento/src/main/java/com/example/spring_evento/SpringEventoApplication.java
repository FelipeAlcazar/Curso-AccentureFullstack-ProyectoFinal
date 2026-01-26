package com.example.spring_evento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringEventoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEventoApplication.class, args);
	}

}
