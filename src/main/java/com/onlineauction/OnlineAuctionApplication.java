package com.onlineauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages={"entity"})
@ComponentScan(basePackages="controller")
public class OnlineAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineAuctionApplication.class, args);
	}
}
