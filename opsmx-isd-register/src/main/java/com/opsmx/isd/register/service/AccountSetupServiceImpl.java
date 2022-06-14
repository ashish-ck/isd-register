package com.opsmx.isd.register.service;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.repositories.UserRepository;
import com.opsmx.isd.register.util.Util;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("AccountSetupService-v1")
@Slf4j
public class AccountSetupServiceImpl implements AccountSetupService {

    @Value("${automation.webhook.url:#{null}}")
    private String automationWebhookURL;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void store(DatasourceRequestModel datasourceRequestModel){
        String emailCheck = datasourceRequestModel.getBusinessEmail();
        try {
            Boolean val = userRepository.existsByBusinessEmail(emailCheck);
            if (!val) {
                userRepository.save(Util.toUser(datasourceRequestModel));
            } else {
                userRepository.findByBusinessEmail(emailCheck);
            }
        }catch (Exception e){
            log.info("ERROR occured during populating data : ", e);
        }
    }

    @Override
    public DatasourceResponseModel setup(DatasourceRequestModel datasourceRequestModel){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = automationWebhookURL;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String,String> uriVariables = new HashMap<>();
        uriVariables.put("user", datasourceRequestModel.getBusinessEmail());
        JSONObject user = new JSONObject();
        user.put("user", datasourceRequestModel.getBusinessEmail());
        String data = user.toString();
        HttpEntity<Object> httpEntity= new HttpEntity<>(data, headers);
        DatasourceResponseModel datasourceResponseModel = new DatasourceResponseModel();
        datasourceResponseModel.setEventProcessed(false);
        datasourceResponseModel.setEventId(UUID.randomUUID().toString());
        try {
            ResponseEntity<DatasourceResponseModel> responseEntity = this.restTemplate.postForEntity(url,
                    httpEntity, DatasourceResponseModel.class, uriVariables);
            if(responseEntity != null) {
                try {
                    DatasourceResponseModel responseModel = responseEntity.getBody();
                    if(responseModel != null && responseModel.getEventProcessed()){
                        log.info("Event trigger ISD register success, event ID = {}", responseModel.getEventId());
                        return responseModel;
                    } else {
                        return datasourceResponseModel;
                    }
                } catch (Exception e) {
                    log.error("Exception in triggering ISD register event {}",e.getMessage());
                }
            }else {
                log.error("Trigger ISD register event failed");
            }
        }catch (Exception e){
            log.error("Exception in ISD register event triggering {}", e.getMessage());
        }
        return datasourceResponseModel;
    }
}

