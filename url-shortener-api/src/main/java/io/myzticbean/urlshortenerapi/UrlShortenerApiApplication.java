package io.myzticbean.urlshortenerapi;

import io.myzticbean.urlshortenerapi.service.kafka.KafkaProducerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UrlShortenerApiApplication implements CommandLineRunner {

	private final KafkaProducerService kafkaProducerService;

    public UrlShortenerApiApplication(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for(int i = 0; i < 100; i++) {
			kafkaProducerService.sendMessage("[" + (i+1) + "] This is a test message");
		}
	}
}
