package com.opsmx.isd.register.service;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service("AccountSetupService-v1")
@Slf4j
public class AccountSetupServiceImpl implements AccountSetupService {

    @Value("${automation.webhook.url:#{null}}")
    private String automationWebhookURL;

    private final RestTemplate restTemplate = new RestTemplate();

    public DatasourceResponseModel setup(DatasourceRequestModel datasourceRequestModel){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = automationWebhookURL;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String,String> uriVariables = new HashMap<>();
        uriVariables.put("user", datasourceRequestModel.getEmail());
        uriVariables.put("email", datasourceRequestModel.getEmail());
        String data = "";
        HttpEntity<Object> httpEntity= new HttpEntity<>(data, headers);
        try {
            ResponseEntity<DatasourceResponseModel> responseEntity = this.restTemplate.postForEntity(url,
                    httpEntity, DatasourceResponseModel.class, uriVariables);
            if(responseEntity != null) {
                try {
                    DatasourceResponseModel responseModel = responseEntity.getBody();
                    if(responseModel.getEventProcessed()){
                        log.info("Event trigger ISD register success, event ID = {}", responseModel.getEventId());
                    } else {
                        throw new RuntimeException("Error triggering ISD register event");
                    }
                    return responseModel;
                } catch (Exception e) {
                    log.error("Exception in triggering ISD register event {}",e.getMessage());
                }
            }else {
                log.error("Trigger ISD register event failed");
            }
        }catch (Exception e){
            log.error("Exception in ISD register event triggering {}", e.getMessage());
        }
        return null;
    }
}
