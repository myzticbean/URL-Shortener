package io.myzticbean.urlshortenerapi.controller;

import io.myzticbean.sharedlibs.dto.model.request.ShortenUrlRequest;
import io.myzticbean.sharedlibs.dto.model.response.ShortenUrlResponse;
import io.myzticbean.urlshortenerapi.service.ShortenUrlService;
import io.myzticbean.urlshortenerapi.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url-shortener/v1")
public class UrlShortenerApiController {

    private final Logger logger = LogManager.getLogger(UrlShortenerApiController.class);

    private final ShortenUrlService shortenUrlService;

    public UrlShortenerApiController(ShortenUrlService shortenUrlService) {
        this.shortenUrlService = shortenUrlService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> testApi(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>("URL Shortener service is up! Your IP is " + httpServletRequest.getRemoteAddr(), HttpStatus.OK);
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest shortenUrlRequest, HttpServletRequest httpServletRequest) {
        String ipAddress = IpUtil.generateRandomIpAddresses();
        if(logger.isInfoEnabled())
            logger.info(new StringBuilder("Request from ")
                            .append(httpServletRequest.getHeader("User-Agent"))
                            .append(" | IpAddress: ")
                            .append(ipAddress).toString());
        ShortenUrlResponse response = shortenUrlService.createShortCode(shortenUrlRequest, httpServletRequest.getRemoteAddr());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
