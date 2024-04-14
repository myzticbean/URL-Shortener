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

import java.util.List;

@Service
public class ShortenedUrlService {

    private static final Logger logger = LogManager.getLogger(ShortenedUrlService.class);

    private final UrlShortenedRepository urlShortenedRepo;

    @Autowired
    public ShortenedUrlService(UrlShortenedRepository urlShortenedRepo) {
        this.urlShortenedRepo = urlShortenedRepo;
    }

    public List<ShortenedUrl> findByShortCode(String shortCode) throws DBServiceException {
        List<ShortenedUrl> shortenedUrls = urlShortenedRepo.findByShortCode(shortCode);
        shortenedUrls.forEach(logger::info);
        return shortenedUrls;
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
        List<ShortenedUrl> shortenedUrls = findByShortCode(shortenedUrl.getShortCode());
        if (!shortenedUrls.isEmpty()) {
            logger.info("ShortCode already exists");
            return mapToDto(shortenedUrls.get(0), false);
        } else {
            return mapToDto(urlShortenedRepo.save(shortenedUrl), true);
        }
    }

    private ShortenedUrl mapFromDto(AddShortenedUrlRequest addShortenedUrlRequest) {
        return new ShortenedUrl(addShortenedUrlRequest.getShortCode(), addShortenedUrlRequest.getFullUrl(), addShortenedUrlRequest.getExpiresAfterInDays());
    }

    private AddShortenedUrlResponse mapToDto(ShortenedUrl shortenedUrl, boolean createdNew) {
        return new AddShortenedUrlResponse(shortenedUrl, createdNew);
    }

}
