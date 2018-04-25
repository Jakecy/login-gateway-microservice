package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimulateLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimulateLoginApplication.class, args);
		System.out.println("................登录微服务启动完成................");
	}

}
