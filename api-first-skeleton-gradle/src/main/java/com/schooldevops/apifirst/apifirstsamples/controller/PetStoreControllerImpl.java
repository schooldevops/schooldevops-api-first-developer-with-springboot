package com.schooldevops.apifirst.apifirstsamples.controller;

import com.schooldevops.apifirst.openapi.domain.Category;
import com.schooldevops.apifirst.openapi.domain.Pet;
import com.schooldevops.apifirst.openapi.rest.PetApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetStoreControllerImpl implements PetApi {

    @Override
    public ResponseEntity<Pet> addPet(Pet pet) {
        return PetApi.super.addPet(pet);
    }

    @Override
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/pet/{petId}",
//            produces = { "application/json" }
//    )
    public ResponseEntity<Pet> getPetById(Long petId) {
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("MyDog");
        Category category = new Category();
        category.setId(1001L);
        category.name("Dog");
        pet.setCategory(category);
        pet.setStatus(Pet.StatusEnum.AVAILABLE);
        pet.setPhotoUrls(List.of("url://http"));

        return ResponseEntity.ok(pet);
    }
}
