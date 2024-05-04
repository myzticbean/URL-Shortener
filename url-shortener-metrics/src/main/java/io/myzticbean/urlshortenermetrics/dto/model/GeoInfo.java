package io.myzticbean.urlshortenermetrics.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
@NoArgsConstructor
public class GeoInfo {
    private String city;
    private String country;
    private String latitude;
    private String longitude;
    private BigInteger visitCount;

    @Override
    public String toString() {
        return "GeoInfo [" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", visitCount=" + visitCount +
                "]";
    }
}