package io.myzticbean.urlshortenerapi.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
public class UrlShortenerException extends RuntimeException {

    private String errorMessage;
}
