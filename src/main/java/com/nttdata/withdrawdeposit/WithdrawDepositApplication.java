package com.nttdata.withdrawdeposit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WithdrawDepositApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithdrawDepositApplication.class, args);
	}

}
