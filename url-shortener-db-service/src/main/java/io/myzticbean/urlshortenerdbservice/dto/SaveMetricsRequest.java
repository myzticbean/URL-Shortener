package io.myzticbean.urlshortenerdbservice.dto;

import io.myzticbean.urlshortenerdbservice.entity.model.VisitInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SaveMetricsRequest {
    private String shortCode;
    private VisitInfo visitInfo;

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public VisitInfo getVisitInfo() {
        return visitInfo;
    }

    public void setVisitInfos(VisitInfo visitInfo) {
        this.visitInfo = visitInfo;
    }

    @Override
    public String toString() {
        return "AddMetricsRequest [shortCode=" + shortCode + ", visitInfo=" + visitInfo + "]";
    }
}
