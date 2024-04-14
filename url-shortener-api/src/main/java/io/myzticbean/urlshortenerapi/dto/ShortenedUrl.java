package io.myzticbean.urlshortenerapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShortenedUrl {

    private String id;

    private String shortCode;

    private String fullUrl;

    private LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiresOn;

    public boolean isExpired() {
        return expiresOn != null && expiresOn.isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "ShortenedUrl [" +
                "id=" + id
                + ", shortCode=" + shortCode
                + ", fullUrl=" + fullUrl
                + ", createdAt=" + createdAt
                + ", expiresOn=" + expiresOn + "]";
    }
}
