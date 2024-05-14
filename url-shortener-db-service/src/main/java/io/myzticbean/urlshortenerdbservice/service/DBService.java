package io.myzticbean.urlshortenerdbservice.service;

import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.dto.SaveMetricsRequest;
import io.myzticbean.urlshortenerdbservice.dto.SaveMetricsResponse;
import io.myzticbean.urlshortenerdbservice.dto.SaveShortenedUrlRequest;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import io.myzticbean.urlshortenerdbservice.entity.UrlMetrics;
import io.myzticbean.urlshortenerdbservice.entity.model.GeoInfo;
import io.myzticbean.urlshortenerdbservice.entity.model.VisitInfo;
import io.myzticbean.urlshortenerdbservice.exception.DBServiceException;
import io.myzticbean.urlshortenerdbservice.repository.UrlMetricsRepository;
import io.myzticbean.urlshortenerdbservice.repository.UrlShortenedRepository;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DBService {

    private static final Logger logger = LogManager.getLogger(DBService.class);

    private final UrlShortenedRepository urlShortenedRepo;
    private final UrlMetricsRepository urlMetricsRepo;

    @Autowired
    public DBService(UrlShortenedRepository urlShortenedRepo, UrlMetricsRepository urlMetricsRepo) {
        this.urlShortenedRepo = urlShortenedRepo;
        this.urlMetricsRepo = urlMetricsRepo;
    }

    private List<ShortenedUrl> findByShortCode(String shortCode) throws DBServiceException {
        try {
            List<ShortenedUrl> shortenedUrls = urlShortenedRepo.findByShortCode(shortCode);
            shortenedUrls.forEach(logger::info);
            return shortenedUrls;
        } catch(DBServiceException e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }

    private List<ShortenedUrl> findByUrl(String fullUrl) throws DBServiceException {
        try {
            List<ShortenedUrl> shortenedUrls = urlShortenedRepo.findByFullUrl(fullUrl);
            shortenedUrls.forEach(logger::info);
            return shortenedUrls;
        } catch (DBServiceException e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }


    @Nullable
    public ShortenedUrl findFirstByShortCode(String shortCode) throws DBServiceException {
        List<ShortenedUrl> shortenedUrls = urlShortenedRepo.findByShortCode(shortCode);
        shortenedUrls.forEach(logger::info);
        if(shortenedUrls.isEmpty()) {
            return null;
        } else {
            return shortenedUrls.get(0);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AddShortenedUrlResponse createShortenedUrlIfNotExist(SaveShortenedUrlRequest dbServiceRequest) throws DBServiceException {
        ShortenedUrl shortenedUrl = mapFromDto(dbServiceRequest);
        // first check if the full url is already added with another short code
        List<ShortenedUrl> shortenedUrls = null;
        if(shortenedUrl.getFullUrl() != null) {
            shortenedUrls = findByUrl(shortenedUrl.getFullUrl());
            if(!shortenedUrls.isEmpty()) {
                // full url already exists -> respond with existing short code
                logger.info("FullUrl already exists");
                return mapToDto(shortenedUrls.get(0), false, true);
            } else {
                // now check if the short code already exists
                if(shortenedUrl.getShortCode() != null) {
                    shortenedUrls = findByShortCode(shortenedUrl.getShortCode());
                    if (!shortenedUrls.isEmpty()) {
                        // short code already exists -> respond with the currently stored full url
                        logger.info("ShortCode already exists");
                        return mapToDto(shortenedUrls.get(0), false, false);
                    } else {
                        // everything unique -> create new record
                        return mapToDto(urlShortenedRepo.save(shortenedUrl), true, false);
                    }
                } else
                    throw new DBServiceException("Could not find short code in request");
            }
        }
        else {
            throw new DBServiceException("Could not find full url in request");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SaveMetricsResponse saveMetrics(SaveMetricsRequest request) throws DBServiceException {
        try {
            // fetch metrics list from db based on short code
            // filter using ip address
            // => if found -> increment the count
            // => if not found -> add to the list
            List<UrlMetrics> urlMetricsList = urlMetricsRepo.findUrlMetricsByShortCode(request.getShortCode());
            UrlMetrics urlMetrics = null;
            if(urlMetricsList.isEmpty()) {
                logger.info("Could not find any url metrics from the provided short code");
                // need to create new record
                GeoInfo reqGeoInfo = request.getVisitInfo().getGeoInfo();
                GeoInfo geoInfo = GeoInfo
                        .builder()
                        .city(reqGeoInfo.getCity())
                        .country(reqGeoInfo.getCountry())
                        .latitude(reqGeoInfo.getLatitude())
                        .longitude(reqGeoInfo.getLongitude())
                        .build();
                VisitInfo visitInfo = VisitInfo
                        .builder()
                        .ipAddress(request.getVisitInfo().getIpAddress())
                        .geoInfo(geoInfo)
                        .build();
                visitInfo.incrementVisitCount();
                urlMetrics = UrlMetrics
                        .builder()
                        .shortCode(request.getShortCode())
                        .visitInfos(new HashSet<>(List.of(visitInfo)))
                        .build();
            } else {
                urlMetrics = urlMetricsList.get(0);
                // filter for the ip address
                Optional<VisitInfo> optional = urlMetrics.getVisitInfos()
                        .stream()
                        .filter(visitInfo -> visitInfo.getIpAddress().equalsIgnoreCase(request.getVisitInfo().getIpAddress()))
                        .findFirst();
                if(optional.isEmpty()) {
                    logger.info("Could not find visit info for IP Address: {}", request.getVisitInfo().getIpAddress());
                    // need to increment and initialize the visit count to 1
                    request.getVisitInfo().incrementVisitCount();
                    urlMetrics.getVisitInfos().add(request.getVisitInfo());
                } else {
                    logger.info("Found visit info for IP Address: {}", request.getVisitInfo().getIpAddress());
                    VisitInfo visitInfo = optional.get();
                    visitInfo.incrementVisitCount();
                    urlMetrics.getVisitInfos().add(visitInfo);
                }
            }
            urlMetricsRepo.save(urlMetrics);
            return SaveMetricsResponse.builder().visitInfos(urlMetrics.getVisitInfos()).build();
        } catch (DBServiceException e) {
            logger.error(e);
            return SaveMetricsResponse.builder().error(e.getMessage()).build();
        }
        // test
        /*{
            List<UrlMetrics> urlMetricsList = urlMetricsRepo.findUrlMetricsByShortCode("rounick");
            if(!urlMetricsList.isEmpty()) {
                urlMetricsList.forEach(logger::info);
            }
            VisitInfo visitInfo1 = VisitInfo
                    .builder()
                    .ipAddress("192.168.0.1")
                    .geoInfo(GeoInfo
                            .builder()
                            .city("TestCity")
                            .country("TestCountry")
                            .latitude("TestLat")
                            .longitude("TestLong")
                            .build())
                    .build();
            visitInfo1.incrementVisitCount();
            VisitInfo visitInfo2 = VisitInfo
                    .builder()
                    .ipAddress("192.168.0.2")
                    .geoInfo(GeoInfo
                            .builder()
                            .city("TestCity1")
                            .country("TestCountry1")
                            .latitude("TestLat1")
                            .longitude("TestLong1")
                            .build())
                    .build();
            visitInfo2.incrementVisitCount();
            visitInfo2.incrementVisitCount();
            Set<VisitInfo> visitInfos = new HashSet<>();
            UrlMetrics urlMetrics = UrlMetrics
                    .builder()
                    .shortCode("rounick")
                    .visitInfos(visitInfos)
                    .build();
            visitInfos.add(visitInfo1);
            visitInfos.add(visitInfo2);
            urlMetricsRepo.save(urlMetrics);
        }*/
    }

    private ShortenedUrl mapFromDto(SaveShortenedUrlRequest saveShortenedUrlRequest) {
        return new ShortenedUrl(saveShortenedUrlRequest.getShortCode(), saveShortenedUrlRequest.getFullUrl(), saveShortenedUrlRequest.getExpiresAfterInDays());
    }

    private AddShortenedUrlResponse mapToDto(ShortenedUrl shortenedUrl, boolean createdNew, boolean duplicate) {
        return new AddShortenedUrlResponse(shortenedUrl, createdNew, duplicate);
    }
}
