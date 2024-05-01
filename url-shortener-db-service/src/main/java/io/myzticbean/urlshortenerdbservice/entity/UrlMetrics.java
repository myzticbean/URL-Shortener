package io.myzticbean.urlshortenerdbservice.entity;

import io.myzticbean.urlshortenerdbservice.entity.model.VisitInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "url-metrics")
@Getter @Setter @NoArgsConstructor
public class UrlMetrics {

    @Id
    private String id;

    private String shortCode;

    private List<VisitInfo> visitInfos;

    @Override
    public String toString() {
        return "UrlVisitMetrics [" +
                "id='" + id + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", visitInfos=" + visitInfos +
                "]";
    }
}
