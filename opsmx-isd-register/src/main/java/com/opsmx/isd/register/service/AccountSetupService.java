package com.opsmx.isd.register.service;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.dto.DatasourceResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AccountSetupService-v1")
public interface AccountSetupService {
    DatasourceResponseModel setup(DatasourceRequestModel datasourceRequestModel);
    @Transactional
    void store(DatasourceRequestModel datasourceRequestModel);
}
