package io.myzticbean.urlshortenerapi.service;

import io.myzticbean.urlshortenerapi.dto.ShortenUrlRequest;
import io.myzticbean.urlshortenerapi.dto.ShortenUrlResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class ShortenUrlService {

    public ShortenUrlResponse createShortCode(ShortenUrlRequest shortenUrlRequest, String ipAddr) {
        return null;
    }

    private String encodeToBase62(String url) {
        return Base64.encodeBase64URLSafeString(url.getBytes());
    }



}
