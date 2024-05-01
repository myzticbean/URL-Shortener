package io.myzticbean.urlshortenermetrics.dto;

import io.myzticbean.urlshortenermetrics.dto.model.VisitInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class AddMetricsResponse {
    private String shortCode;
    private VisitInfo visitInfo;
    private String errorMsg;
}
