package io.myzticbean.urlshortenermetrics.service.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LogManager.getLogger(KafkaConsumerService.class);
    private static final String TOPIC = "add-metrics";

    @KafkaListener(topics = TOPIC, groupId = "my-group")
    public void receiveMessage(String message) {
        // Process the received message
        logger.info("Received message: {}", message);
    }
}
