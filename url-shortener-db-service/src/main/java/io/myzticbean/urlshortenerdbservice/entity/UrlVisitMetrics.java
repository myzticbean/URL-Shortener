package io.myzticbean.urlshortenerdbservice.entity;

import io.myzticbean.urlshortenerdbservice.entity.model.GeoInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "url-metrics")
@Getter @Setter @NoArgsConstructor
public class UrlVisitMetrics {

    @Id
    private String id;

    private String shortCode;

    private GeoInfo geoInfo;

    @Override
    public String toString() {
        return "UrlVisitMetrics [" +
                "id='" + id + '\'' +
                ", shortCode='" + shortCode + '\'' +
                ", geoInfo=" + geoInfo +
                "]";
    }
}
