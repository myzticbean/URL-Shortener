package io.myzticbean.urlshortenermetrics.service;

import io.myzticbean.sharedlibs.dto.model.GeoInfo;
import io.myzticbean.sharedlibs.dto.model.VisitInfo;
import io.myzticbean.sharedlibs.dto.model.request.AddMetricsRequest;
import io.myzticbean.sharedlibs.dto.model.request.SaveMetricsRequest;
import io.myzticbean.sharedlibs.dto.model.response.SaveMetricsResponse;
import io.myzticbean.urlshortenermetrics.exception.UrlMetricsException;
import io.myzticbean.urlshortenermetrics.feignclient.dbservice.DBServiceFeignClient;
import io.myzticbean.urlshortenermetrics.util.GeoIPUtil;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MetricService {

    private static final Logger logger = LogManager.getLogger(MetricService.class);

    private final DBServiceFeignClient dbServiceFeignClient;
    private final GeoIPUtil geoIPUtil;

    public MetricService(DBServiceFeignClient dbServiceFeignClient, GeoIPUtil geoIPUtil) {
        this.dbServiceFeignClient = dbServiceFeignClient;
        this.geoIPUtil = geoIPUtil;
    }

    @Nullable
    public SaveMetricsResponse addMetricsToDB(AddMetricsRequest request) throws UrlMetricsException {
        try {
            if(!StringUtils.hasLength(request.getIpAddress()) || !StringUtils.hasLength(request.getShortCode())) {
                throw new UrlMetricsException("IP address or short code is required");
            }
            GeoInfo geoInfo = geoIPUtil.findGeoDetailsFromIpAddress(request.getIpAddress());
            ResponseEntity<SaveMetricsResponse> response = dbServiceFeignClient
                    .saveMetricsHandler(mapToDto(request.getIpAddress(), request.getShortCode(), geoInfo));
            if(!response.getStatusCode().equals(HttpStatus.OK)) {
                logger.error("Failed to save metrics to DB");
                throw new UrlMetricsException("Failed to save metrics to DB");
            }
            logger.info("Successfully saved metrics to DB: {}", response.getBody());
            return response.getBody();
        } catch (UrlMetricsException e) {
            logger.error(e);
            throw e;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    private SaveMetricsRequest mapToDto(String ipAddress, String shortCode, GeoInfo geoInfo) {
        VisitInfo visitInfo = VisitInfo.builder()
                .geoInfo(geoInfo)
                .ipAddress(ipAddress)
                .build();
        return SaveMetricsRequest.builder()
                .shortCode(shortCode)
                .visitInfo(visitInfo)
                .build();
    }

}
