package io.myzticbean.urlshortenerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class AddShortenedUrlRequest {
    private String shortCode;
    private String fullUrl;
    private Long expiresAfterInDays;
}
