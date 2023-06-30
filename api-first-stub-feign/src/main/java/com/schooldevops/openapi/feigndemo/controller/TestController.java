package com.schooldevops.openapi.feigndemo.controller;

import com.schooldevops.openapi.feigndemo.services.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    PushService pushService;

    @GetMapping("/test")
    public ResponseEntity<?> getTest() {
        return pushService.getAppPush("testId");
    }
}
