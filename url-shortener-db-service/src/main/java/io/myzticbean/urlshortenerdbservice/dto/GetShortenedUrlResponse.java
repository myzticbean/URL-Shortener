package io.myzticbean.urlshortenerdbservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class GetShortenedUrlResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ShortenedUrl shortenedUrl;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;
}
