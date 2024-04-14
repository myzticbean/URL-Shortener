package io.myzticbean.urlshortenerdbservice.dto;

import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class GetShortenedUrlResponse {
    private ShortenedUrl shortenedUrl;
    private String error;
}
