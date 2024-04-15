package io.myzticbean.urlshortenerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class AddShortenedUrlResponse {
    private ShortenedUrl shortenedUrl;
    private boolean created;
    private boolean duplicateUrl;
}
