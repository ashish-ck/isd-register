package com.opsmx.isd.register.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@ToString
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Company name is mandatory")
    private String companyName;

    @NotBlank(message = "Phone is mandatory")
    private String contactNumber;

    @NotBlank(message = "Email is mandatory")
    private String businessEmail;

    public User(String firstName, String lastName, String companyName, String contactNumber, String businessEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.contactNumber = contactNumber;
        this.businessEmail = businessEmail;
    }
}

