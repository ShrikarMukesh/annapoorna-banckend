package com.annapoorna.restaurantservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.annapoorna.restaurantservice.entity.Restaurant;
import com.annapoorna.restaurantservice.service.RestaurantService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable String id) {
      
    	Restaurant restaurant = restaurantService.getRestaurantById(id);
    	
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        restaurant.updateAverageRating(); // Automatically calculate average rating
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Restaurant>> createRestaurants(@Valid @RequestBody List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            restaurant.updateAverageRating(); // Automatically calculate average rating
        }
        List<Restaurant> createdRestaurants = restaurantService.createRestaurants(restaurants);
        return new ResponseEntity<>(createdRestaurants, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable String id, @Valid @RequestBody Restaurant updatedRestaurant) {
        Restaurant restaurant = restaurantService.updateRestaurant(id, updatedRestaurant);
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
