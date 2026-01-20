package com.annapoorna.restaurantservice.service;

import com.annapoorna.restaurantservice.entity.RestaurantDTO;
import com.annapoorna.restaurantservice.repository.RestaurantRepositoryV2;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceV2Impl implements RestaurantServiceV2 {

	private final RestaurantRepositoryV2 restaurantRepositoryV2;

	public RestaurantServiceV2Impl(RestaurantRepositoryV2 restaurantRepositoryV2) {
		this.restaurantRepositoryV2 = restaurantRepositoryV2;
	}

	@Override
	public RestaurantDTO createRestaurantV2(RestaurantDTO restaurant) {
		return restaurantRepositoryV2.save(restaurant);
	}
}
