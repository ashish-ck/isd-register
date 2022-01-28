package com.opsmx.isd.register.util;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.entities.User;

public class Util {
    public static DatasourceRequestModel toDatasourceRequestModel(User user) {
        DatasourceRequestModel requestModel = new DatasourceRequestModel();
        requestModel.setFirstName(user.getFirstName());
        requestModel.setLastName(user.getLastName());
        requestModel.setCompanyName(user.getCompanyName());
        requestModel.setBusinessEmail(user.getBusinessEmail());
        requestModel.setContactNumber(user.getContactNumber());
        return requestModel;
    }

    public static User toUser(DatasourceRequestModel requestModel){
        User user = new User();
        user.setFirstName(requestModel.getFirstName());
        user.setLastName(requestModel.getLastName());
        user.setCompanyName(requestModel.getCompanyName());
        user.setBusinessEmail(requestModel.getBusinessEmail());
        user.setContactNumber(requestModel.getContactNumber());
        return user;
    }
}
