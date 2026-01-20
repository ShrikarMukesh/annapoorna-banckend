package com.annapoorna.restaurantservice.repository;

import com.annapoorna.restaurantservice.entity.RestaurantDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepositoryV2 extends MongoRepository<RestaurantDTO, String> {

}
