package com.ultimalabs.sattrackapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SattrackapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SattrackapiApplication.class, args);
	}
}
