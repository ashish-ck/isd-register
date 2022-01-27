package com.opsmx.isd.register.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class DatasourceRequestModel {
    private String FirstName;
    private String LastName;
    private String CompanyName;
    private String ContactNumber;
    private String BusinessEmail;
}
