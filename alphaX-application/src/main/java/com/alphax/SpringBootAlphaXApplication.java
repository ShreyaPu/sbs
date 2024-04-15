package com.alphax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching //enables Spring Caching functionality
public class SpringBootAlphaXApplication {
	private static final Logger logger = LogManager.getLogger(SpringBootAlphaXApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAlphaXApplication.class, args);
		logger.info("Start AlphaX");
		
	}
}