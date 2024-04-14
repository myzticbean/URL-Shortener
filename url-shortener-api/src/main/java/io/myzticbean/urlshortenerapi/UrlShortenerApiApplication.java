package io.myzticbean.urlshortenerapi;

import io.myzticbean.urlshortenerapi.service.ShortenUrlService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UrlShortenerApiApplication implements CommandLineRunner {

	private final Logger logger = LogManager.getLogger(UrlShortenerApiApplication.class);

	private final ShortenUrlService shortenUrlService;

    public UrlShortenerApiApplication(ShortenUrlService shortenUrlService) {
        this.shortenUrlService = shortenUrlService;
    }

    public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
