package com.opsmx.isd.register.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatasourceResponseModel {
    private Boolean eventProcessed;
    private String eventId;
}
