package io.myzticbean.urlshortenerdbservice.controller;

import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlRequest;
import io.myzticbean.urlshortenerdbservice.dto.AddShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.dto.GetShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.entity.ShortenedUrl;
import io.myzticbean.urlshortenerdbservice.exception.DBServiceException;
import io.myzticbean.urlshortenerdbservice.service.DBService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/db-service/v1")
public class DBController {

    private final DBService dbService;

    private final String NO_RESULTS_ERROR_MSG = "No results found";

    public DBController(DBService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddShortenedUrlResponse> addToDB(@RequestBody AddShortenedUrlRequest dbServiceRequest) {
        try {
            AddShortenedUrlResponse dbServiceResponse = dbService.createShortenedUrlIfNotExist(dbServiceRequest);
            if(dbServiceResponse.isCreated())
                return new ResponseEntity<>(dbServiceResponse, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(dbServiceResponse, HttpStatus.OK);
        } catch(DBServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{shortCode}")
    public ResponseEntity<GetShortenedUrlResponse> getShortenedUrl(@PathVariable String shortCode) {
        ShortenedUrl shortenedUrl = dbService.findFirstByShortCode(shortCode);
        if(shortenedUrl == null)
            return new ResponseEntity<>(GetShortenedUrlResponse.builder().error(NO_RESULTS_ERROR_MSG).build(), HttpStatus.OK);
        else
            return new ResponseEntity<>(GetShortenedUrlResponse.builder().shortenedUrl(shortenedUrl).build(), HttpStatus.OK);
    }

}
