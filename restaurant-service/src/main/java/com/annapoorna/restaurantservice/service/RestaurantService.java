package com.annapoorna.restaurantservice.service;

import java.util.List;

import com.annapoorna.restaurantservice.entity.Restaurant;

public interface RestaurantService {

	List<Restaurant> getAllRestaurants();

	Restaurant getRestaurantById(String id);

	Restaurant createRestaurant(Restaurant restaurant);

	Restaurant updateRestaurant(String id, Restaurant updatedRestaurant);

	void deleteRestaurant(String id);
}
