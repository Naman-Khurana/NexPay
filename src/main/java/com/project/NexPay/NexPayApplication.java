package com.project.NexPay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NexPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexPayApplication.class, args);
	}

}
