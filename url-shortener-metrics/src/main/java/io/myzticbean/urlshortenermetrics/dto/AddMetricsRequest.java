package io.myzticbean.urlshortenermetrics.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AddMetricsRequest {
    private String shortCode;
    private String ipAddress;
}
