package com.opsmx.isd.register.controller;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.UserData;
import com.opsmx.isd.register.service.AccountSetupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
public class AccountManagerController {

    @Autowired
	private AccountSetupService accountSetupService;

    @Value("${progresspage:#{null}}")
    private String progressPage;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(DatasourceRequestModel user) {
        accountSetupService.setup(toUserData(user));
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(progressPage)).build();
    }

    private UserData toUserData(DatasourceRequestModel datasourceRequestModel){
        UserData userData = new UserData();
        return userData;
    }
}