package io.myzticbean.sharedlibs.dto.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Objects;

@AllArgsConstructor @NoArgsConstructor
public class VisitInfo {

    private String ipAddress;
    private GeoInfo geoInfo;
    private BigInteger visitCount;

    private VisitInfo(VisitInfoBuilder visitInfoBuilder) {
        this.ipAddress = visitInfoBuilder.ipAddress;
        this.geoInfo = visitInfoBuilder.geoInfo;
        this.visitCount = visitInfoBuilder.visitCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitInfo visitInfo = (VisitInfo) o;
        return Objects.equals(getIpAddress(), visitInfo.getIpAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIpAddress());
    }

    public void incrementVisitCount() {
        if(this.visitCount == null) {
            this.visitCount = BigInteger.ONE;
        } else {
            this.visitCount = this.visitCount.add(BigInteger.ONE);
        }
    }

    public String toString() {
        return "VisitInfo [ipAddress=" + ipAddress + ", geoInfo=" + geoInfo + "visitCount=" + visitCount + "]";
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public GeoInfo getGeoInfo() {
        return geoInfo;
    }

    public void setGeoInfo(GeoInfo geoInfo) {
        this.geoInfo = geoInfo;
    }

    public BigInteger getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(BigInteger visitCount) {
        this.visitCount = visitCount;
    }

    public static VisitInfoBuilder builder() {
        return new VisitInfoBuilder();
    }

    public static class VisitInfoBuilder {
        private String ipAddress;
        private GeoInfo geoInfo;
        private BigInteger visitCount;

        public VisitInfoBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public VisitInfoBuilder geoInfo(GeoInfo geoInfo) {
            this.geoInfo = geoInfo;
            return this;
        }

        public VisitInfoBuilder visitCount(BigInteger visitCount) {
            this.visitCount = visitCount;
            return this;
        }

        public VisitInfo build() {
            return new VisitInfo(this);
        }
    }
}
