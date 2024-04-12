package io.myzticbean.urlshortenerdbservice.entity;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = "shortened-url")
@Getter
@Setter
@NoArgsConstructor
public class ShortenedUrl {

    @Id
    private String id;

    private String shortCode;

    private String fullUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @com.mongodb.lang.Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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

    public void setExpiresIn(Long daysAfterToday) {
        this.expiresOn = LocalDateTime.now().plusDays(daysAfterToday);
    }

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
