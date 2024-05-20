package io.myzticbean.sharedlibs.dto.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor
public class AddMetricsRequest implements Serializable {
    private String shortCode;
    private String ipAddress;
}
