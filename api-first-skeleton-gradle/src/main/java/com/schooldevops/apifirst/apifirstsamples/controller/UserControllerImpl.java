package com.schooldevops.apifirst.apifirstsamples.controller;

import com.schooldevops.apifirst.openapi.petstore.domain.User;
import com.schooldevops.apifirst.openapi.petstore.rest.UserApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserApi {
    @Override
    public ResponseEntity<Void> createUser(User user) {
        return UserApi.super.createUser(user);
    }
}
