package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AdminLoginApplication {

	public static void main(String[] args) {

		SpringApplication.run(AdminLoginApplication.class, args);
		System.out.println("................admin登录微服务启动完成................");
	}

}
