package io.myzticbean.urlshortenerdbservice;

import com.mongodb.client.MongoClient;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import io.myzticbean.urlshortenerdbservice.service.ShortenedUrlService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class UrlShortenerDbServiceApplication implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(UrlShortenerDbServiceApplication.class);

	@Autowired
	private ShortenedUrlService shortenedUrlService;

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerDbServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// @TODO: ONLY FOR TESTING; REMOVE LATER
//		logger.info("ShortCode created: " + shortenedUrlService.createShortenedUrlIfNotExist(new ShortenedUrl(
//				"rounick",
//				"https://www.myzticbean.io",
//				290L)));
	}
}
