package io.myzticbean.urlshortenerapi.service;

import io.myzticbean.urlshortenerapi.dto.*;
import io.myzticbean.urlshortenerapi.exception.UrlShortenerException;
import io.myzticbean.urlshortenerapi.feignclient.UrlShortenerDBServiceClient;
import io.myzticbean.urlshortenerapi.util.Base64Util;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        return saveShortCode(request);
    }

    private String generateShortCodeFromUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String shortCode = Base64Util.encodeToBase62(uuid).substring(0, SHORT_CODE_LENGTH);
        logger.warn("New ShortCode created: {}", shortCode);
        shortCode = "MTMyOTZkOG";
        return shortCode;
    }

    private ShortenUrlResponse saveShortCode(AddShortenedUrlRequest request) {
        ResponseEntity<AddShortenedUrlResponse> response = dbServiceClient.addShortCodeToDB(request);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            // new short code was added to DB -> ALL GOOD
            logger.info("Successfully added ShortCode");
            return mapToDto(response.getBody() != null ? response.getBody() : null, null);
        } else if(response.getStatusCode() == HttpStatus.OK) {
            // new short code collided with an existing one
            return processAddShortenedUrlResponseForCollision(request, response);
        } else {
            logger.warn("HttpStatus code not recognized: {}", response.getStatusCode());
            return mapToDto(
                    new AddShortenedUrlResponse(null, false, false),
                    "HttpStatus code not recognized: " + response.getStatusCode());
        }
    }

    /**
     * SCENARIO 1: If shortcode exists and url also matches -> No issues -> Return the same details as response;
     * SCENARIO 2: If shortcode doesn't exist but url exists -> No issues -> Return the url with the existing short code
     * SCENARIO 2: If shortcode exists and url different -> shortcode collision -> generate new shortcode (3 retries)
     * @param response Response
     */
    private ShortenUrlResponse processAddShortenedUrlResponseForCollision(
            AddShortenedUrlRequest request, ResponseEntity<AddShortenedUrlResponse> response) {
        try {
            AddShortenedUrlResponse responseBody  = response.getBody();
            if(responseBody != null && responseBody.getShortenedUrl() != null) {
                ShortenedUrl shortenedUrl = responseBody.getShortenedUrl();
                boolean isShortCodeMatch = shortenedUrl.getShortCode() != null && shortenedUrl.getShortCode().equals(request.getShortCode());
                boolean isFullUrlMatch = shortenedUrl.getFullUrl() != null && shortenedUrl.getFullUrl().equals(request.getFullUrl());
                if(isShortCodeMatch && isFullUrlMatch) {
                    // SCENARIO 1
                    return mapToDto(responseBody, null);
                } else if(!isShortCodeMatch && isFullUrlMatch) {
                    // SCENARIO 2
                    return mapToDto(
                            new AddShortenedUrlResponse(
                                    response.getBody().getShortenedUrl(), false, true),
                            null);
                } else if(!isShortCodeMatch) {
                    // SCENARIO 3
                    retryCounter.set(0);
                    while(retryCounter.get() < SHORT_CODE_COLLISION_RETRY_COUNT
                            && response.getStatusCode().equals(HttpStatus.OK)) {
                        // check if url also collided -> Then ALL GOOD -> Url already exists with same short code (somehow?)
                        if(!responseBody.getShortenedUrl().getFullUrl().equals(request.getShortCode())) {
                            // Url did not collide; only short code collided -> Need a new short code
                            retryCounter.set(retryCounter.get() + 1);
                            response = dbServiceClient.addShortCodeToDB(request);
                            if(response.getStatusCode() == HttpStatus.CREATED) {
                                // new short code was created -> break out of loop
                                break;
                            }
                        } else {
                            // new short code was created -> break out of loop
                            break;
                        }
                    }
                    return mapToDto(response.getBody(), null);
                }
            }
        } catch (UrlShortenerException e) {
            logger.error(e.getMessage(), e);
        } finally {
            retryCounter.remove();
        }
        return null;
    }

    private ShortenUrlResponse mapToDto(AddShortenedUrlResponse responseBody, String errorMessage) throws UrlShortenerException {
        if(responseBody == null || responseBody.getShortenedUrl() == null)
            throw new UrlShortenerException("ResponseBody/ShortenedUrl is null");
        return new ShortenUrlResponse(responseBody.getShortenedUrl().getShortCode(),
                responseBody.getShortenedUrl().getCreatedAt(),
                responseBody.getShortenedUrl().getExpiresOn(), errorMessage);
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
