package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FrontendBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontendBankApplication.class, args);
	}

}
