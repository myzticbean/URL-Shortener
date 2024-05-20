package io.myzticbean.sharedlibs.dto.model.request;

import io.myzticbean.sharedlibs.dto.model.VisitInfo;
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

    public SaveMetricsRequest(SaveMetricsRequestBuilder builder) {
        this.shortCode = builder.shortCode;
        this.visitInfo = builder.visitInfo;
    }

    public static SaveMetricsRequestBuilder builder() {
        return new SaveMetricsRequestBuilder();
    }

    public static class SaveMetricsRequestBuilder {
        private String shortCode;
        private VisitInfo visitInfo;

        public SaveMetricsRequestBuilder shortCode(String shortCode) {
            this.shortCode = shortCode;
            return this;
        }

        public SaveMetricsRequestBuilder visitInfo(VisitInfo visitInfo) {
            this.visitInfo = visitInfo;
            return this;
        }

        public SaveMetricsRequest build() {
            return new SaveMetricsRequest(this);
        }
    }
}
