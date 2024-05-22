package io.myzticbean.sharedlibs.dto.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ShortenUrlRequest {

    private String urlToForward;
    private Long expiryDays;

}
