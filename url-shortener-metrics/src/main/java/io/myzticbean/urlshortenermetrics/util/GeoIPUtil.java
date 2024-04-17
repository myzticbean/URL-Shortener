package io.myzticbean.urlshortenermetrics.util;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;

@Component
public class GeoIPUtil {

//    private static final GeoIPUtil geoIPUtil = null;
    private DatabaseReader dbReader;

    public GeoIPUtil() {
        try {
            String dbLocation = "/GeoIPDB/GeoLite2-City.mmdb";
            File database = new File(dbLocation);
            this.dbReader = new DatabaseReader.Builder(database).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public GeoIPUtil getInstance() {
//        if(geoIPUtil == null) {
//            try {
//                String dbLocation = "/GeoIPDB/GeoLite2-City.mmdb";
//                File database = new File(dbLocation);
//                this.dbReader = new DatabaseReader.Builder(database).build();
//                return geoIPUtil;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void findIpDetails(String ipAddr) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ipAddr);
            dbReader.city(ipAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
