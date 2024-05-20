package io.myzticbean.urlshortenerapi.service.kafka;

import io.myzticbean.sharedlibs.dto.model.request.AddMetricsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, AddMetricsRequest> kafkaTemplate;

    @Value("${url-shortener-metrics-api.kafka-topic}")
    private String topic;

    public KafkaProducerService(KafkaTemplate<String, AddMetricsRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AddMetricsRequest message) {
        kafkaTemplate.send(topic, message);
    }
}
