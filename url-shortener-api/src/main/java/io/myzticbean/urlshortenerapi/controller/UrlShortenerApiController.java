package io.myzticbean.urlshortenerapi.controller;

import io.myzticbean.urlshortenerapi.dto.ShortenUrlRequest;
import io.myzticbean.urlshortenerapi.dto.ShortenUrlResponse;
import io.myzticbean.urlshortenerapi.service.ShortenUrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShortenerApiController {

    private ShortenUrlService shortenUrlService;

    public UrlShortenerApiController(ShortenUrlService shortenUrlService) {
        this.shortenUrlService = shortenUrlService;
    }

    @GetMapping("/api/test")
    public ResponseEntity<String> testApi(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>("URL Shortener service is up! Your IP is " + httpServletRequest.getRemoteAddr(), HttpStatus.OK);
    }

    @PostMapping("/api/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest shortenUrlRequest, HttpServletRequest httpServletRequest) {
        shortenUrlService.createShortCode(shortenUrlRequest, httpServletRequest.getRemoteAddr());
        return null;
    }


}
