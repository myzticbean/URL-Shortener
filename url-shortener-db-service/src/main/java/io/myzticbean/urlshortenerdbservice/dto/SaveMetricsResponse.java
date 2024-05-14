package io.myzticbean.urlshortenerdbservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.myzticbean.urlshortenerdbservice.entity.model.VisitInfo;

import java.util.Set;

public class SaveMetricsResponse {

    private Set<VisitInfo> visitInfos;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;

    public Set<VisitInfo> getVisitInfos() {
        return visitInfos;
    }

    public void setVisitInfos(Set<VisitInfo> visitInfos) {
        this.visitInfos = visitInfos;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SaveMetricsResponse() {
    }

    public SaveMetricsResponse(SaveMetricsResponseBuilder responseBuilder) {
        this.visitInfos = responseBuilder.visitInfos;
        this.error = responseBuilder.error;
    }

    public static SaveMetricsResponseBuilder builder() {
        return new SaveMetricsResponseBuilder();
    }

    public static class SaveMetricsResponseBuilder {
        private Set<VisitInfo> visitInfos;
        private String error;

        public SaveMetricsResponseBuilder visitInfos(Set<VisitInfo> visitInfos) {
            this.visitInfos = visitInfos;
            return this;
        }

        public SaveMetricsResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public SaveMetricsResponse build() {
            return new SaveMetricsResponse(this);
        }
    }
}
