package io.myzticbean.sharedlibs.dto.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.myzticbean.sharedlibs.dto.model.ShortenedUrl;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class GetShortenedUrlResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ShortenedUrl shortenedUrl;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;
}
