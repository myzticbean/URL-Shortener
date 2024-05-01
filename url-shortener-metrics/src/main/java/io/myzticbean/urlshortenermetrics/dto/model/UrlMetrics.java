package io.myzticbean.urlshortenermetrics.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UrlMetrics {
    private String id;
    private String shortCode;
    private List<VisitInfo> visitInfos;
}
