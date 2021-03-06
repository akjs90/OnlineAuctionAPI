package com.onlineauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages={"entity"})
@ComponentScan(basePackages={"controller","service","security"})
@EnableJpaRepositories(basePackages="repository")
@EnableScheduling
public class OnlineAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineAuctionApplication.class, args);
	}
}
