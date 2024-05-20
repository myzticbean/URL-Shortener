package io.myzticbean.sharedlibs.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShortenedUrl {

    private String shortCode;

    private String fullUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresOn;

    public ShortenedUrl(String shortCode, String fullUrl) {
        this.shortCode = shortCode;
        this.fullUrl = fullUrl;
        this.createdAt = LocalDateTime.now();
    }

    public ShortenedUrl(String shortCode, String fullUrl, Long daysAfterToday) {
        this.shortCode = shortCode;
        this.fullUrl = fullUrl;
        this.createdAt = LocalDateTime.now();
        setExpiresIn(daysAfterToday);
    }

    public ShortenedUrl(ShortenedUrlBuilder shortenedUrlBuilder) {
        this.shortCode = shortenedUrlBuilder.shortCode;
        this.fullUrl = shortenedUrlBuilder.fullUrl;
        this.createdAt = shortenedUrlBuilder.createdAt;
        this.expiresOn = shortenedUrlBuilder.expiresOn;
    }

    public void setExpiresIn(Long daysAfterToday) {
        this.expiresOn = LocalDateTime.now().plusDays(daysAfterToday);
    }

    public boolean isExpired() {
        return expiresOn != null && expiresOn.isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "ShortenedUrl [" +
                "shortCode=" + shortCode
                + ", fullUrl=" + fullUrl
                + ", createdAt=" + createdAt
                + ", expiresOn=" + expiresOn + "]";
    }

    public static ShortenedUrlBuilder builder() {
        return new ShortenedUrlBuilder();
    }

    public static class ShortenedUrlBuilder {
        private String shortCode;
        private String fullUrl;
        private LocalDateTime createdAt;
        private LocalDateTime expiresOn;

        public ShortenedUrlBuilder shortCode(String shortCode) {
            this.shortCode = shortCode;
            return this;
        }

        public ShortenedUrlBuilder fullUrl(String fullUrl) {
            this.fullUrl = fullUrl;
            return this;
        }

        public ShortenedUrlBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ShortenedUrlBuilder expiresOn(LocalDateTime expiresOn) {
            this.expiresOn = expiresOn;
            return this;
        }

        public ShortenedUrl build() {
            return new ShortenedUrl(this);
        }
    }
}
