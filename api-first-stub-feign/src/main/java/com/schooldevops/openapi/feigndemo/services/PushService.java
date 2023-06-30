package com.schooldevops.openapi.feigndemo.services;

import com.schooldevops.openapi.feigndemo.api.CommonAlarmApiClient;
import com.schooldevops.openapi.feigndemo.client.model.AppPush;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PushService {
    @Autowired
    CommonAlarmApiClient apiClient;

    public ResponseEntity<AppPush> getAppPush(String id) {
        try {
            return apiClient.getAppPush(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
