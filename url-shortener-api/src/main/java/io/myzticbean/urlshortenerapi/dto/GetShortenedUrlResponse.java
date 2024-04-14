package io.myzticbean.urlshortenerapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class GetShortenedUrlResponse {
    private ShortenedUrl shortenedUrl;
    private String error;
}
