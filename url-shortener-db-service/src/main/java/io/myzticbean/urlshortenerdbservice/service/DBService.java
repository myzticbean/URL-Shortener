package io.myzticbean.urlshortenerdbservice.service;

import io.myzticbean.sharedlibs.dto.model.GeoInfo;
import io.myzticbean.sharedlibs.dto.model.ShortenedUrl;
import io.myzticbean.sharedlibs.dto.model.VisitInfo;
import io.myzticbean.sharedlibs.dto.model.request.SaveMetricsRequest;
import io.myzticbean.sharedlibs.dto.model.request.SaveShortenedUrlRequest;
import io.myzticbean.sharedlibs.dto.model.response.SaveMetricsResponse;
import io.myzticbean.sharedlibs.dto.model.response.SaveShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrlDAO;
import io.myzticbean.urlshortenerdbservice.entity.UrlMetrics;
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
import java.util.stream.Collectors;

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

    private List<ShortenedUrlDAO> findByShortCode(String shortCode) throws DBServiceException {
        try {
            List<ShortenedUrlDAO> shortenedUrls = urlShortenedRepo.findByShortCode(shortCode);
            shortenedUrls.forEach(logger::info);
            //return shortenedUrls.stream().map(this::mapDaoToDto).collect(Collectors.toList());
            return shortenedUrls;
        } catch(DBServiceException e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }

    private List<ShortenedUrlDAO> findByUrl(String fullUrl) throws DBServiceException {
        try {
            List<ShortenedUrlDAO> shortenedUrls = urlShortenedRepo.findByFullUrl(fullUrl);
            shortenedUrls.forEach(logger::info);
            return shortenedUrls;
        } catch (DBServiceException e) {
            logger.error(e);
            return new ArrayList<>();
        }
    }


    @Nullable
    public ShortenedUrl findFirstByShortCode(String shortCode) throws DBServiceException {
        List<ShortenedUrlDAO> shortenedUrls = urlShortenedRepo.findByShortCode(shortCode);
        shortenedUrls.forEach(logger::info);
        if(shortenedUrls.isEmpty()) {
            return null;
        } else {
            // return shortenedUrls.get(0);
            return mapDaoToDto(shortenedUrls.getFirst());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SaveShortenedUrlResponse createShortenedUrlIfNotExist(SaveShortenedUrlRequest dbServiceRequest) throws DBServiceException {
        ShortenedUrlDAO shortenedUrlDAO = mapToDaoFromRequest(dbServiceRequest);
        // first check if the full url is already added with another short code
        List<ShortenedUrlDAO> shortenedUrlDAOS = null;
        if(shortenedUrlDAO.getFullUrl() != null) {
            shortenedUrlDAOS = findByUrl(shortenedUrlDAO.getFullUrl());
            if(!shortenedUrlDAOS.isEmpty()) {
                // full url already exists -> respond with existing short code
                logger.info("FullUrl already exists");
                return mapToRequest(mapDaoToDto(shortenedUrlDAOS.getFirst()), false, true);
            } else {
                // now check if the short code already exists
                if(shortenedUrlDAO.getShortCode() != null) {
                    shortenedUrlDAOS = findByShortCode(shortenedUrlDAO.getShortCode());
                    if (!shortenedUrlDAOS.isEmpty()) {
                        // short code already exists -> respond with the currently stored full url
                        logger.info("ShortCode already exists");
                        return mapToRequest(mapDaoToDto(shortenedUrlDAOS.getFirst()), false, false);
                    } else {
                        // everything unique -> create new record
                        return mapToRequest(urlShortenedRepo.save(shortenedUrlDAO), true, false);
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

    private ShortenedUrlDAO mapToDaoFromRequest(SaveShortenedUrlRequest saveShortenedUrlRequest) {
        return new ShortenedUrlDAO(saveShortenedUrlRequest.getShortCode(), saveShortenedUrlRequest.getFullUrl(), saveShortenedUrlRequest.getExpiresAfterInDays());
    }

    private SaveShortenedUrlResponse mapToRequest(ShortenedUrl shortenedUrl, boolean createdNew, boolean duplicate) {
        return new SaveShortenedUrlResponse(shortenedUrl, createdNew, duplicate);
    }

    private SaveShortenedUrlResponse mapToRequest(ShortenedUrlDAO shortenedUrl, boolean createdNew, boolean duplicate) {
        return new SaveShortenedUrlResponse(mapDaoToDto(shortenedUrl), createdNew, duplicate);
    }

    private ShortenedUrl mapDaoToDto(ShortenedUrlDAO dao) {
        return ShortenedUrl.builder().shortCode(dao.getShortCode()).fullUrl(dao.getFullUrl()).createdAt(dao.getCreatedAt()).expiresOn(dao.getExpiresOn()).build();
    }
}
