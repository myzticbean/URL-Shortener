package io.myzticbean.urlshortenerapi.controller;

import io.myzticbean.urlshortenerapi.service.ShortenUrlService;
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

    public ShortenedUrlNavigationController(ShortenUrlService shortenUrlService) {
        this.shortenUrlService = shortenUrlService;
    }

    @GetMapping("/{short-code}")
    public Object navigateUrl(@PathVariable(value = "short-code") String shortCode) {
        if(logger.isInfoEnabled())
            logger.info("Short code: {}", shortCode);
        // service call to fetch the url
        String fullUrl = shortenUrlService.fetchFullUrl(shortCode);
        if(fullUrl != null)
            return new RedirectView(fullUrl);
        else
            return new ResponseEntity<>("Url not found or is expired", HttpStatus.OK);
    }

}
