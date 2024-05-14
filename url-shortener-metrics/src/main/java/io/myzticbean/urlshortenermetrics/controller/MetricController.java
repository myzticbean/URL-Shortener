package io.myzticbean.urlshortenermetrics.controller;

import io.myzticbean.urlshortenermetrics.dto.AddMetricsRequest;
import io.myzticbean.urlshortenermetrics.dto.AddMetricsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics-service/v1")
public class MetricController {

    private static final Logger logger = LogManager.getLogger(MetricController.class);

    @PostMapping("/add-metrics")
    public ResponseEntity<AddMetricsResponse> addMetrics(@RequestBody AddMetricsRequest request) {
        // @TODO: add logic
        return null;
    }

}
