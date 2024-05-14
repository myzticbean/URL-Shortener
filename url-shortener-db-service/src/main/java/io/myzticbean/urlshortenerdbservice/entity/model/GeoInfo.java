package io.myzticbean.urlshortenerdbservice.entity.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
public class GeoInfo {

    private String city;
    private String country;
    private String latitude;
    private String longitude;

    private GeoInfo(GeoInfoBuilder geoInfoBuilder) {
        this.city = geoInfoBuilder.city;
        this.country = geoInfoBuilder.country;
        this.latitude = geoInfoBuilder.latitude;
        this.longitude = geoInfoBuilder.longitude;
    }

    @Override
    public String toString() {
        return "GeoInfo[city=" + city + ", country=" + country + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static GeoInfoBuilder builder() {
        return new GeoInfoBuilder();
    }

    public static class GeoInfoBuilder {
        private String city;
        private String country;
        private String latitude;
        private String longitude;

        public GeoInfoBuilder city(String city) {
            this.city = city;
            return this;
        }

        public GeoInfoBuilder country(String country) {
            this.country = country;
            return this;
        }

        public GeoInfoBuilder latitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public GeoInfoBuilder longitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public GeoInfo build() {
            return new GeoInfo(this);
        }
    }
}
