package com.opsmx.isd.register.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@NoArgsConstructor
@ToString
public class DatasourceRequestModel {
    private String firstName;
    private String lastName;
    private String companyName;
    private String contactNumber;
    private String businessEmail;
    private Date createdAt;
    private Date updatedAt;
}
