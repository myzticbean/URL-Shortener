package io.myzticbean.sharedlibs.dto.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.myzticbean.sharedlibs.dto.model.VisitInfo;
import lombok.*;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class AddMetricsResponse {
    private String shortCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private VisitInfo visitInfo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorMsg;
}
