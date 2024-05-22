package io.myzticbean.urlshortenermetrics.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import io.myzticbean.sharedlibs.dto.model.GeoInfo;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;

@Component
public class GeoIPUtil {

    private DatabaseReader dbReader;

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${url-shortener-metrics-api.geoIpDb-location}")
    private String geoIpDbLocation;

    private static final Logger logger = LogManager.getLogger(GeoIPUtil.class);

    @PostConstruct
    private void initialize() {
        try {
            String dbLocation = "classpath:GeoIPDB/GeoLite2-City.mmdb";
            Resource resource = resourceLoader.getResource(dbLocation);
            File database = resource.getFile();
            logger.warn(database.getAbsolutePath());
            dbReader = new DatabaseReader.Builder(database).build();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Nullable
    public GeoInfo findGeoDetailsFromIpAddress(String ipAddr) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ipAddr);
            CityResponse cityResponse = dbReader.city(ipAddress);
            GeoInfo geoInfo = GeoInfo.builder()
                    .city(cityResponse.getCity().getName())
                    .country(cityResponse.getCountry().getName())
                    .latitude(Double.toString(cityResponse.getLocation().getLatitude()))
                    .longitude(Double.toString(cityResponse.getLocation().getLongitude()))
                    .build();
            logger.warn(geoInfo);
            return geoInfo;
        } catch (GeoIp2Exception ee) {
            logger.error(ee.getMessage(), ee);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
