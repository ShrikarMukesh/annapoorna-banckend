package com.annapoorna.restaurantservice.repository;

import com.annapoorna.restaurantservice.entity.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    // Custom query methods, if needed
}
