package com.sh303.circle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CircleBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(CircleBootstrap.class, args);
	}

}
