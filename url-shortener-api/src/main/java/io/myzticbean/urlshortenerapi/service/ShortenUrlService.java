package io.myzticbean.urlshortenerapi.service;

import io.myzticbean.urlshortenerapi.dto.*;
import io.myzticbean.urlshortenerapi.feignclient.UrlShortenerDBServiceClient;
import io.myzticbean.urlshortenerapi.util.Base64Util;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShortenUrlService {

    private final Logger logger = LogManager.getLogger(ShortenUrlService.class);

    private final UrlShortenerDBServiceClient dbServiceClient;

    private final int SHORT_CODE_LENGTH = 10;
    private final int SHORT_CODE_COLLISION_RETRY_COUNT = 3;

    private ThreadLocal<Integer> retryCounter = new ThreadLocal<>();

    public ShortenUrlService(UrlShortenerDBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    public ShortenUrlResponse createShortCode(ShortenUrlRequest shortenUrlRequest, String ipAddr) {
        AddShortenedUrlRequest request = mapToAddShortenedUrlRequest(shortenUrlRequest);
        // create short code
        String shortCode = generateShortCodeFromUUID();
        request.setShortCode(shortCode);
        // save to DB
        saveShortCode(request);
        return null;
    }

    private String generateShortCodeFromUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortCode = Base64Util.encodeToBase62(uuid).substring(0, SHORT_CODE_LENGTH);
        logger.warn("New ShortCode created: {}", shortCode);
        return shortCode;
    }

    private void saveShortCode(AddShortenedUrlRequest request) {
        retryCounter.set(1);
        ResponseEntity<AddShortenedUrlResponse> response = dbServiceClient.addToDB(request);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            // new short code was added to DB -> ALL GOOD
            logger.info("Successfully added ShortCode");
        } else if(response.getStatusCode() == HttpStatus.OK) {
            // new short code collided with an existing one
            // check if url has also collided
            if(response.getBody() != null) {
                AddShortenedUrlResponse responseBody = response.getBody();
                while(retryCounter.get() < SHORT_CODE_COLLISION_RETRY_COUNT) {
                    // check if url also collided -> Then ALL GOOD -> Url already exists with same short code (somehow?)
                    if(!responseBody.getShortenedUrl().getFullUrl().equals(request.getShortCode())) {
                        // Url did not collide; only short code collided -> Need a new short code
                        retryCounter.set(retryCounter.get() + 1);
                        response = dbServiceClient.addToDB(request);
                        if(response.getStatusCode() == HttpStatus.CREATED) {
                            break;
                        }
                    } else {
                        break;
                    }
                }

            }
        } else {

        }
        retryCounter.remove();
    }

    @Nullable
    public String fetchFullUrl(String shortCode) {
        ResponseEntity<GetShortenedUrlResponse> response = dbServiceClient.getShortenedUrl(shortCode);
        if(response.getStatusCode().equals(HttpStatus.OK)
                && response.getBody() != null
                && checkFullUrlExistsOrExpired(response.getBody())) {
            String fullUrl = response.getBody().getShortenedUrl().getFullUrl();
            logger.info(fullUrl);
            return fullUrl;
        } else {
            logger.error("Url not found or is expired");
        }
        return null;
    }

    private boolean checkFullUrlExistsOrExpired(GetShortenedUrlResponse responseBody) {
        return responseBody.getShortenedUrl() != null
                && responseBody.getShortenedUrl().getFullUrl() != null
                && !responseBody.getShortenedUrl().isExpired();
    }

    private AddShortenedUrlRequest mapToAddShortenedUrlRequest(ShortenUrlRequest shortenUrlRequest) {
        AddShortenedUrlRequest addShortenedUrlRequest = new AddShortenedUrlRequest();
        addShortenedUrlRequest.setFullUrl(shortenUrlRequest.getUrlToForward());
        addShortenedUrlRequest.setExpiresAfterInDays(shortenUrlRequest.getExpiryDays());
        return addShortenedUrlRequest;
    }


}
