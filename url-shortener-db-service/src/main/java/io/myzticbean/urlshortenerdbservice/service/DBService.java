package io.myzticbean.urlshortenerdbservice.service;

import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlRequest;
import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import io.myzticbean.urlshortenerdbservice.exception.DBServiceException;
import io.myzticbean.urlshortenerdbservice.repository.UrlShortenedRepository;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBService {

    private static final Logger logger = LogManager.getLogger(DBService.class);

    private final UrlShortenedRepository urlShortenedRepo;

    @Autowired
    public DBService(UrlShortenedRepository urlShortenedRepo) {
        this.urlShortenedRepo = urlShortenedRepo;
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
    public AddShortenedUrlResponse createShortenedUrlIfNotExist(AddShortenedUrlRequest dbServiceRequest) throws DBServiceException {
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

    private ShortenedUrl mapFromDto(AddShortenedUrlRequest addShortenedUrlRequest) {
        return new ShortenedUrl(addShortenedUrlRequest.getShortCode(), addShortenedUrlRequest.getFullUrl(), addShortenedUrlRequest.getExpiresAfterInDays());
    }

    private AddShortenedUrlResponse mapToDto(ShortenedUrl shortenedUrl, boolean createdNew, boolean duplicate) {
        return new AddShortenedUrlResponse(shortenedUrl, createdNew, duplicate);
    }
}
