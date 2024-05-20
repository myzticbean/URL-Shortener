package io.myzticbean.urlshortenerdbservice.repository;

import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrlDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlShortenedRepository extends MongoRepository<ShortenedUrlDAO, String> {

    List<ShortenedUrlDAO> findByShortCode(String shortCode);

    List<ShortenedUrlDAO> findByFullUrl(String url);



}
