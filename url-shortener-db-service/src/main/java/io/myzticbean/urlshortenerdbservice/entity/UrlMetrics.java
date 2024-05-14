package io.myzticbean.urlshortenerdbservice.entity;

import io.myzticbean.urlshortenerdbservice.entity.model.VisitInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "url-metrics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UrlMetrics {

    @Id
    private String id;
    private String shortCode;
    private Set<VisitInfo> visitInfos;

    @Override
    public String toString() {
        return "UrlVisitMetrics [" +
                "id='" + id + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", visitInfos=" + visitInfos +
                "]";
    }
}
