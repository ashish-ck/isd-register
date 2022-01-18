package com.opsmx.isd.register.controller;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.service.AccountSetupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class AccountManagerController {
    @Autowired
	private AccountSetupService accountSetupService;

    @Value("${redirect.url:#{null}}")
    private String redirectURL;

    @PostMapping(value = "/webhookTrigger", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DatasourceResponseModel> register(@RequestBody DatasourceRequestModel dataSourceRequestModel,
                                                            HttpServletRequest request) {
        log.info(dataSourceRequestModel.toString());
        DatasourceResponseModel datasourceResponseModel = accountSetupService.setup(dataSourceRequestModel);
        log.info(datasourceResponseModel.toString());
        return new ResponseEntity<>(datasourceResponseModel, HttpStatus.CREATED);
    }
}