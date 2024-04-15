package io.myzticbean.urlshortenerdbservice;

import io.myzticbean.urlshortenerdbservice.service.DBService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShortenerDbServiceApplication implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(UrlShortenerDbServiceApplication.class);

	@Autowired
	private DBService DBService;

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerDbServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
