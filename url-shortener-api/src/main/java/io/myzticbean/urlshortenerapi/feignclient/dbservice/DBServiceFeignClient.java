package io.myzticbean.urlshortenerapi.feignclient.dbservice;

import io.myzticbean.sharedlibs.dto.model.request.AddShortenedUrlRequest;
import io.myzticbean.sharedlibs.dto.model.response.AddShortenedUrlResponse;
import io.myzticbean.sharedlibs.dto.model.response.GetShortenedUrlResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "dbService", url = "localhost:8082/api/db-service/v1")
public interface DBServiceFeignClient {

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    @GetMapping("/get/{shortCode}")
    ResponseEntity<GetShortenedUrlResponse> getShortenedUrl(@PathVariable String shortCode);

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    @PostMapping("/save-url")
    ResponseEntity<AddShortenedUrlResponse> addShortCodeToDB(@RequestBody AddShortenedUrlRequest dbServiceRequest);

}
