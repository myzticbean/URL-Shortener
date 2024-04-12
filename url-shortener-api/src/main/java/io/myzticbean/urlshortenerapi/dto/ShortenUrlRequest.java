package io.myzticbean.urlshortenerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @AllArgsConstructor
public class ShortenUrlRequest {

    private String urlToForward;
    private int expiryDays;

}
