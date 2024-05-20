package io.myzticbean.sharedlibs.dto.model.response;

import io.myzticbean.sharedlibs.dto.model.ShortenedUrl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class SaveShortenedUrlResponse {
    private ShortenedUrl shortenedUrl;
    private boolean created;
    private boolean duplicateUrl;
}
