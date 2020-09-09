package com.neo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableDiscoveryClient
// @EnableFeignClients
public class ProducerApplication {
	// --spring.profiles.active=peer
	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Autowired
	private Environment env;

}