package io.myzticbean.urlshortenerdbservice.controller;

import io.myzticbean.sharedlibs.dto.model.ShortenedUrl;
import io.myzticbean.sharedlibs.dto.model.request.SaveMetricsRequest;
import io.myzticbean.sharedlibs.dto.model.request.SaveShortenedUrlRequest;
import io.myzticbean.sharedlibs.dto.model.response.GetShortenedUrlResponse;
import io.myzticbean.sharedlibs.dto.model.response.SaveMetricsResponse;
import io.myzticbean.sharedlibs.dto.model.response.SaveShortenedUrlResponse;
import io.myzticbean.urlshortenerdbservice.exception.DBServiceException;
import io.myzticbean.urlshortenerdbservice.service.DBService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/db-service/v1")
public class DBController {

    private final DBService dbService;

    private final String NO_RESULTS_ERROR_MSG = "No results found";

    private static final Logger logger = LogManager.getLogger(DBController.class);

    public DBController(DBService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/save-url")
    public ResponseEntity<
            SaveShortenedUrlResponse> saveUrlHandler(@RequestBody SaveShortenedUrlRequest dbServiceRequest) {
        try {
            SaveShortenedUrlResponse dbServiceResponse = dbService.createShortenedUrlIfNotExist(dbServiceRequest);
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
        try {
            ShortenedUrl shortenedUrl = dbService.findFirstByShortCode(shortCode);
            if(shortenedUrl == null)
                return new ResponseEntity<>(GetShortenedUrlResponse.builder().error(NO_RESULTS_ERROR_MSG).build(), HttpStatus.OK);
            else
                return new ResponseEntity<>(GetShortenedUrlResponse.builder().shortenedUrl(shortenedUrl).build(), HttpStatus.OK);
        } catch (DBServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save-metrics")
    public ResponseEntity<SaveMetricsResponse> saveMetricsHandler(@RequestBody SaveMetricsRequest saveMetricsRequest) {
        try {
            SaveMetricsResponse response = dbService.saveMetrics(saveMetricsRequest);
            if(response != null && response.getVisitInfos() != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if(response != null && response.getError() != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch(DBServiceException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
