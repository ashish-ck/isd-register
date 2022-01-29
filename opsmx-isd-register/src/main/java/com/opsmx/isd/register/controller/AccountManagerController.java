package com.opsmx.isd.register.controller;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.dto.Message;
import com.opsmx.isd.register.dto.SaasTrialResponseModel;
import com.opsmx.isd.register.repositories.UserRepository;
import com.opsmx.isd.register.service.AccountSetupService;
import com.opsmx.isd.register.service.SendMessage;
import com.opsmx.isd.register.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@Slf4j
public class AccountManagerController {
    @Autowired
	private AccountSetupService accountSetupService;

    @Value("${redirect.url:#{null}}")
    private String redirectURL;

    private final UserRepository userRepository;

    @Autowired
    private SendMessage sendMessage;

    private final Long TIMEOUT_IN_SECONDS = 120L;

    @Autowired
    public AccountManagerController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/webhookTrigger", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaasTrialResponseModel> webhookTrigger(@RequestBody DatasourceRequestModel dataSourceRequestModel,
                                                            HttpServletRequest request) {
        log.info(dataSourceRequestModel.toString());
        userRepository.save(Util.toUser(dataSourceRequestModel));
        log.info("User data saved ");
        AtomicReference<Boolean> isSpinnakerSetupComplete = new AtomicReference<>(false);
        AtomicReference<DatasourceResponseModel> atomicReference = new AtomicReference<>();
        CompletableFuture.supplyAsync(() -> {
            atomicReference.set(accountSetupService.setup(dataSourceRequestModel));
            return atomicReference;
        }).orTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).whenComplete((result, exception) -> {
            if(exception != null) {
                sendMessage.sendMessageObject(new Message(dataSourceRequestModel.getBusinessEmail(), exception.getMessage()));
                return;
            }
            DatasourceResponseModel responseModel = atomicReference.get();
            if(responseModel != null && responseModel.getEventProcessed()){
                log.info("Building spinnaker setup for user {} ", dataSourceRequestModel.getBusinessEmail());
                isSpinnakerSetupComplete.set(true);
                // send message to redirect to login page.
                sendMessage.sendMessageObject(new Message(dataSourceRequestModel.getBusinessEmail(), "success"));
            }else {
                log.info("Error building spinnaker for user {}", dataSourceRequestModel.getBusinessEmail());
                // send message to redirect to error page.
                sendMessage.sendMessageObject(new Message(dataSourceRequestModel.getBusinessEmail(), "failure"));
            }
        });
        SaasTrialResponseModel saasTrialResponseModel = new SaasTrialResponseModel();
        saasTrialResponseModel.setEventProcessed(true);
        saasTrialResponseModel.setEventId(UUID.randomUUID().toString());
        return new ResponseEntity<>(saasTrialResponseModel, HttpStatus.CREATED);
    }
}