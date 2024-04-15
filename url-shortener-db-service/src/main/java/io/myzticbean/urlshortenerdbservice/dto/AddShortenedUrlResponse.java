package io.myzticbean.urlshortenerdbservice.dto;

import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class AddShortenedUrlResponse {
    private ShortenedUrl shortenedUrl;
    private boolean created;
    private boolean duplicateUrl;
}
