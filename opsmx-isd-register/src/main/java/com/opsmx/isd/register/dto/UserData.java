package com.opsmx.isd.register.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserData {
    private String firstName;
    private String lastName;
    private String companyName;
    private String phone;
    private String email;
    private String user;
}
