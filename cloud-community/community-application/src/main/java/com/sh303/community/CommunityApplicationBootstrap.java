package com.sh303.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @program: cloud-community
 * @description: 云社区服务主程序入口
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CommunityApplicationBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplicationBootstrap.class, args);
	}

}
