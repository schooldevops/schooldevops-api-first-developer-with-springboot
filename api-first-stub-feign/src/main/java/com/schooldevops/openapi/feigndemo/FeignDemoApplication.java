package com.schooldevops.openapi.feigndemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FeignDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignDemoApplication.class, args);
	}

}
