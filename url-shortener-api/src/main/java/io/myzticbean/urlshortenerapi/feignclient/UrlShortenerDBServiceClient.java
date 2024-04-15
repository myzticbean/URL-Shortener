package io.myzticbean.urlshortenerapi.feignclient;

import io.myzticbean.urlshortenerapi.dto.AddShortenedUrlRequest;
import io.myzticbean.urlshortenerapi.dto.AddShortenedUrlResponse;
import io.myzticbean.urlshortenerapi.dto.GetShortenedUrlResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "dbService", url = "localhost:8082/api/db-service/v1")
public interface UrlShortenerDBServiceClient {

    @GetMapping("/get/{shortCode}")
    ResponseEntity<GetShortenedUrlResponse> getShortenedUrl(@PathVariable String shortCode);

    @PostMapping("/add")
    ResponseEntity<AddShortenedUrlResponse> addShortCodeToDB(@RequestBody AddShortenedUrlRequest dbServiceRequest);

}
