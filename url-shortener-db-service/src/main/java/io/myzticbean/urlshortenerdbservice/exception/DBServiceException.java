package io.myzticbean.urlshortenerdbservice.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
public class DBServiceException extends RuntimeException {
    private String errorMessage;
}
