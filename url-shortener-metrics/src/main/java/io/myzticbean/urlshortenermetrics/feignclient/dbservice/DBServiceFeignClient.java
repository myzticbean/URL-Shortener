package io.myzticbean.urlshortenermetrics.feignclient.dbservice;

import io.myzticbean.sharedlibs.dto.model.request.SaveMetricsRequest;
import io.myzticbean.sharedlibs.dto.model.response.SaveMetricsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "dbService", url = "localhost:8082/api/db-service/v1")
public interface DBServiceFeignClient {

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    @PostMapping("/save-metrics")
    ResponseEntity<SaveMetricsResponse> saveMetricsHandler(@RequestBody SaveMetricsRequest saveMetricsRequest);

}
