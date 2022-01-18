package com.opsmx.isd.register.service;

import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.dto.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AccountSetupService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void setup(UserData userData){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "https://spin-gate.canis.isd.opsmx.org/webhooks/ondemandsaas";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        Map<String,String> uriVariables = new HashMap<>();
        uriVariables.put("user", userData.getUser());
        uriVariables.put("email", userData.getEmail());
        String data = "";
        HttpEntity<Object> httpEntity= new HttpEntity<>(data, headers);
        try {
            ResponseEntity<DatasourceResponseModel> responseEntity = this.restTemplate.postForEntity(url,
                    httpEntity, DatasourceResponseModel.class, uriVariables);
            if(responseEntity != null) {
                try {
                    DatasourceResponseModel responseModel = responseEntity.getBody();
                    if(responseModel.getEventProcessed()){
                        log.info("Event trigger ISD register success {}", responseModel.getEventId());
                    } else {
                        throw new RuntimeException("Error triggering ISD register event");
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
    }
}
