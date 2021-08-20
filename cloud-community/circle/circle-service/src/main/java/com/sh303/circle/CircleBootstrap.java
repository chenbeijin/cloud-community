package com.sh303.circle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: cloud-community
 * @description: 圈子服务主程序入口
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@SpringBootApplication
public class CircleBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(CircleBootstrap.class, args);
	}

}
