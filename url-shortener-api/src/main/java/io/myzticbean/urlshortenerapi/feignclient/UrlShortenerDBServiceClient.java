package io.myzticbean.urlshortenerapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "dbService", url = "localhost:8082")
public interface UrlShortenerDBServiceClient {
}
