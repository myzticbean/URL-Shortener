package io.myzticbean.urlshortenerdbservice.controller;

import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlRequest;
import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.dto.GetShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import io.myzticbean.urlshortenerdbservice.service.ShortenedUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/db-service/v1")
public class DBController {

    private final ShortenedUrlService shortenedUrlService;

    private final String NO_RESULTS_ERROR_MSG = "No results found";

    public DBController(ShortenedUrlService shortenedUrlService) {
        this.shortenedUrlService = shortenedUrlService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddShortenedUrlResponse> addToDB(@RequestBody AddShortenedUrlRequest dbServiceRequest) {
        AddShortenedUrlResponse dbServiceResponse = shortenedUrlService.createShortenedUrlIfNotExist(dbServiceRequest);
        if(dbServiceResponse.isCreated())
            return new ResponseEntity<>(dbServiceResponse, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(dbServiceResponse, HttpStatus.OK);

    }

    @GetMapping("/get/{shortCode}")
    public ResponseEntity<GetShortenedUrlResponse> getShortenedUrl(@PathVariable String shortCode) {
        ShortenedUrl shortenedUrl = shortenedUrlService.findFirstByShortCode(shortCode);
        if(shortenedUrl == null)
            return new ResponseEntity<>(GetShortenedUrlResponse.builder().error(NO_RESULTS_ERROR_MSG).build(), HttpStatus.OK);
        else
            return new ResponseEntity<>(GetShortenedUrlResponse.builder().shortenedUrl(shortenedUrl).build(), HttpStatus.OK);
    }

}
