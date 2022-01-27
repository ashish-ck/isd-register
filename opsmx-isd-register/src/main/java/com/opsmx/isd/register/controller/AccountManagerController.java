package com.opsmx.isd.register.controller;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.entities.User;
import com.opsmx.isd.register.repositories.UserRepository;
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

    private final UserRepository userRepository;

    @Autowired
    public AccountManagerController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/webhookTrigger", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DatasourceResponseModel> register(@RequestBody DatasourceRequestModel dataSourceRequestModel,
                                                            HttpServletRequest request) {
        log.info(dataSourceRequestModel.toString());
        DatasourceResponseModel datasourceResponseModel = accountSetupService.setup(dataSourceRequestModel);
        log.info(datasourceResponseModel.toString());
        userRepository.save(toUser(dataSourceRequestModel));
        log.info("User data saved ");
        return new ResponseEntity<>(datasourceResponseModel, HttpStatus.CREATED);
    }

    private User toUser(DatasourceRequestModel requestModel){
        User user = new User();
        user.setEmail(requestModel.getEmail());
        user.setPhone(requestModel.getPhone());
        user.setFirstName(requestModel.getFirstName());
        user.setLastName(requestModel.getLastName());
        user.setCompanyName(requestModel.getCompanyName());
        return user;
    }

    private DatasourceRequestModel toDataSourceRequestModel(User user) {
        DatasourceRequestModel requestModel = new DatasourceRequestModel();
        requestModel.setEmail(user.getEmail());
        requestModel.setPhone(user.getPhone());
        requestModel.setFirstName(user.getFirstName());
        requestModel.setLastName(user.getLastName());
        requestModel.setCompanyName(user.getCompanyName());
        return requestModel;
    }
}