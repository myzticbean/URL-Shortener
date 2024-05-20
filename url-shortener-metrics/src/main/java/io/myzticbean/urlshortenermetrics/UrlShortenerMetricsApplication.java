package io.myzticbean.urlshortenermetrics;

import io.myzticbean.urlshortenermetrics.util.GeoIPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UrlShortenerMetricsApplication implements CommandLineRunner {

	@Autowired
	private GeoIPUtil geoIPUtil;

	@Override
	public void run(String... args) throws Exception {
		geoIPUtil.findGeoDetailsFromIpAddress("49.43.201.142");
	}

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerMetricsApplication.class, args);
	}

}
