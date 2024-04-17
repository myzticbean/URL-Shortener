package io.myzticbean.urlshortenerapi.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UrlShortenerException extends RuntimeException {
    private String errorMessage;

    public UrlShortenerException(String errorMessage) {
        super(errorMessage);
    }
}
