package io.myzticbean.urlshortenerapi.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

public class Base64Util {

    private static final Logger logger = LogManager.getLogger(Base64Util.class);

    private Base64Util() {
        throw new IllegalStateException("Utility class");
    }

    public static String encodeToBase62UrlSafe(String url) {
        logger.info(url);
        return Base64.encodeBase64URLSafeString(url.getBytes());
    }

    public static String encodeToBase62(String url) {
        logger.info(url);
        return Base64.encodeBase64String(url.getBytes());
    }

}
