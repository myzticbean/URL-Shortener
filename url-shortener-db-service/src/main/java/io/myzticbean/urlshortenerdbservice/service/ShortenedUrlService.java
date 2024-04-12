package io.myzticbean.urlshortenerdbservice.service;

import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import io.myzticbean.urlshortenerdbservice.exception.DBServiceException;
import io.myzticbean.urlshortenerdbservice.repository.UrlShortenedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShortenedUrlService {

    private final MongoTemplate mongoTemplate;  // Use final for immutability

    private final UrlShortenedRepository urlShortenedRepo;

    @Autowired
    public ShortenedUrlService(UrlShortenedRepository urlShortenedRepo, MongoTemplate mongoTemplate) {
        this.urlShortenedRepo = urlShortenedRepo;
        this.mongoTemplate = mongoTemplate;
    }

    public List<ShortenedUrl> findByShortCode(String shortCode) throws DBServiceException {
        List<ShortenedUrl> shortenedUrls = urlShortenedRepo.findByShortCode(shortCode);
//        ShortenedUrl shortenedUrl1 = mongoTemplate.findById(shortCode, ShortenedUrl.class);
        shortenedUrls.forEach(System.out::println);
        return shortenedUrls;
    }

    /*@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShortenedUrl createShortenedUrl(ShortenedUrl shortenedUrl) throws DBServiceException {
        shortenedUrl = urlShortenedRepo.save(shortenedUrl);
        System.out.println("ShortenedUrl created: " + shortenedUrl);
        return shortenedUrl;
    }*/

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ShortenedUrl createShortenedUrlIfNotExist(ShortenedUrl shortenedUrl) throws DBServiceException {
        List<ShortenedUrl> shortenedUrls = findByShortCode(shortenedUrl.getShortCode());
        if (!shortenedUrls.isEmpty()) {
            System.out.println("ShortCode already exists");
            return null;
        } else {
            return urlShortenedRepo.save(shortenedUrl);
        }
    }

}
