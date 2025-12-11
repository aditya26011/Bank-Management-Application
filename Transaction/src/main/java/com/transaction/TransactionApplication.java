package com.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("com.transaction")
@EnableFeignClients
public class TransactionApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(TransactionApplication.class, args);
		System.out.println("TransactionApplication Started Successfully...*****");
	}

}
