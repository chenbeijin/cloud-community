package com.sh303.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CommunityApplicationBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplicationBootstrap.class, args);
	}

}
