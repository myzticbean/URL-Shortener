package io.myzticbean.urlshortenermetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UrlShortenerMetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerMetricsApplication.class, args);
	}

}
