package io.myzticbean.urlshortenerdbservice.repository;

import io.myzticbean.urlshortenerdbservice.entity.UrlMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UrlMetricsRepository extends MongoRepository<UrlMetrics, String> {
    List<UrlMetrics> findUrlMetricsByShortCode(String shortCode);
}
