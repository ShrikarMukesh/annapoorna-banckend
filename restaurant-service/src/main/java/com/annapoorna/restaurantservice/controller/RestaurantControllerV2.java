package com.annapoorna.restaurantservice.controller;

import com.annapoorna.restaurantservice.entity.RestaurantDTO;
import com.annapoorna.restaurantservice.service.RestaurantServiceV2;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantControllerV2 {

    @Autowired
    private RestaurantServiceV2 restaurantServiceV2;

    @PostMapping("/create")
    public ResponseEntity<RestaurantDTO> createRestaurantV2(@RequestBody RestaurantDTO restaurant) {
        RestaurantDTO createdRestaurant = restaurantServiceV2.createRestaurantV2(restaurant);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }
}
