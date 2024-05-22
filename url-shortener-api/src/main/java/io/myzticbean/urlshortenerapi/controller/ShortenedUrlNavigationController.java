package io.myzticbean.urlshortenerapi.controller;

import io.myzticbean.sharedlibs.dto.model.request.AddMetricsRequest;
import io.myzticbean.urlshortenerapi.service.ShortenUrlService;
import io.myzticbean.urlshortenerapi.service.kafka.KafkaProducerService;
import io.myzticbean.urlshortenerapi.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ShortenedUrlNavigationController {

    private final Logger logger = LogManager.getLogger(ShortenedUrlNavigationController.class);

    private final ShortenUrlService shortenUrlService;

    private final KafkaProducerService kafkaProducerService;

    public ShortenedUrlNavigationController(ShortenUrlService shortenUrlService, KafkaProducerService kafkaProducerService) {
        this.shortenUrlService = shortenUrlService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/{short-code}")
    public Object navigateUrl(@PathVariable(value = "short-code") String shortCode, HttpServletRequest httpServletRequest) {
        if(logger.isInfoEnabled())
            logger.info("Short code: {}", shortCode);
        // service call to fetch the url
        String fullUrl = shortenUrlService.fetchFullUrl(shortCode);
        if(fullUrl != null) {
            String ipAddress = httpServletRequest.getRemoteAddr();
            ipAddress = IpUtil.generateRandomIpAddresses();
            kafkaProducerService.sendMessage(new AddMetricsRequest(shortCode, ipAddress));
            return new RedirectView(fullUrl);
        }
        else
            return new ResponseEntity<>("Url not found or is expired", HttpStatus.OK);
    }

}
