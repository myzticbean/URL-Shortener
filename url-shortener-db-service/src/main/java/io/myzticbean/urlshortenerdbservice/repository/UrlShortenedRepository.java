package io.myzticbean.urlshortenerdbservice.repository;

import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlShortenedRepository extends MongoRepository<ShortenedUrl, String> {

    List<ShortenedUrl> findByShortCode(String shortCode);

    List<ShortenedUrl> findByFullUrl(String url);



}
