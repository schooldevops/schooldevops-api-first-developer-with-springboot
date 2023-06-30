package com.schooldevops.apifirst.apifirstsamples.controller;

import com.schooldevops.apifirst.apifirstsamples.domain.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetControllerImpl implements PetApi {
    @Override
    public ResponseEntity<Pet> getPetById(Long petId) {
        // TODO.. 생성된 pet Rest api 에 해당하는 비즈니스 로직을 작성하자.
        return PetApi.super.getPetById(petId);
    }
}
