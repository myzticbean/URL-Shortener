package io.myzticbean.urlshortenerdbservice.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class VisitInfo {

    private String ipAddress;
    private GeoInfo geoInfo;

    public String toString() {
        return "VisitInfo [ipAddress=" + ipAddress + ", geoInfo=" + geoInfo + "]";
    }

}
