package io.myzticbean.sharedlibs.dto.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class AddShortenedUrlRequest {
    private String shortCode;
    private String fullUrl;
    private Long expiresAfterInDays;
}
