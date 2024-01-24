package com.kss.stockDiscussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class StockDiscussionApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockDiscussionApplication.class, args);
	}

}
