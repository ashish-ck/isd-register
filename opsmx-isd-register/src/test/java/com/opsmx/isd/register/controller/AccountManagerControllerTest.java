package com.opsmx.isd.register.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import com.opsmx.isd.register.service.AccountSetupService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class AccountManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountSetupService accountSetupService;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testRegister() throws Exception {
        DatasourceResponseModel responseModel = new DatasourceResponseModel();
        responseModel.setEventProcessed(true);
        responseModel.setEventId("3ba2ba54-6f83-466f-9b05-831d765794f8");
        Mockito.when(accountSetupService.setup(ArgumentMatchers.any())).thenReturn(responseModel);
        String json = mapper.writeValueAsString(responseModel);
        mockMvc.perform(post("/webhookTrigger").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventProcessed", Matchers.equalTo(true)))
                .andExpect(jsonPath("$.eventId", Matchers.equalTo("3ba2ba54-6f83-466f-9b05-831d765794f8")));
    }
}