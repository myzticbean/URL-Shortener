package io.myzticbean.urlshortenermetrics.exception;

public class UrlMetricsException extends RuntimeException {

    private String errorMessage;

    public UrlMetricsException(String errorMessage) {
        super(errorMessage);
    }
}
