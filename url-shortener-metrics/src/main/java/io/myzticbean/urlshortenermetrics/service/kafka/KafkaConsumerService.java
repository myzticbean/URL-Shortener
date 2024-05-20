package io.myzticbean.urlshortenermetrics.service.kafka;

import io.myzticbean.sharedlibs.dto.model.request.AddMetricsRequest;
import io.myzticbean.urlshortenermetrics.service.MetricService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LogManager.getLogger(KafkaConsumerService.class);
    private static final String TOPIC = "add-metrics";

    private final MetricService metricService;

    public KafkaConsumerService(MetricService metricService) {
        this.metricService = metricService;
    }

    @KafkaListener(topics = TOPIC, groupId = "my-group")
    public void receiveMessage(AddMetricsRequest message) {
        // Process the received message
        logger.info("Received message: {}", message);

    }
}
