package io.myzticbean.urlshortenerdbservice.entity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter @NoArgsConstructor
public class GeoInfo {

    // This will be the unique identifier
    private String ipAddress;
    private String city;
    private String latitude;
    private String longitude;
    // Visit count from this IP address
    private BigInteger visitCount;

    @Override
    public String toString() {
        return "GeoInfo [" +
                "ipAddress='" + ipAddress + '\'' +
                ", city='" + city + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", visitCount=" + visitCount +
                "]";
    }
}
