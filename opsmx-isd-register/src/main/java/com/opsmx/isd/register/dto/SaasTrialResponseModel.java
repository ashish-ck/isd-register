package com.opsmx.isd.register.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class SaasTrialResponseModel {
    private Boolean eventProcessed;
    private String eventId;
}