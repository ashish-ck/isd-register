package com.opsmx.isd.register.util;

import com.opsmx.isd.register.dto.DatasourceRequestModel;
import com.opsmx.isd.register.entities.User;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;

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

    private static Bucket createNewBucket() {
        long capacity = 10;
        Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    public static Boolean rateLimit(HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession(true);
        String appKey = String.valueOf("9466eb06-fe8a-4b4e-9f1d-3025f7225085");
        Bucket bucket = (Bucket) session.getAttribute("throttler-" + appKey);
        if (bucket == null) {
            bucket = createNewBucket();
            session.setAttribute("throttler-" + appKey, bucket);
        }
        boolean okToGo = bucket.tryConsume(1);
        return okToGo;
    }
}
