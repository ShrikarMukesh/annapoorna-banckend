package com.annapoorna.restaurantservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.annapoorna.restaurantservice.entity.Restaurant;
import com.annapoorna.restaurantservice.repository.RestaurantRepository;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	private final RestaurantRepository restaurantRepository;

	@Autowired
	public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	@Override
	public Restaurant getRestaurantById(String id) {
		return restaurantRepository.findById(id).orElse(null);
	}

	@Override
	public Restaurant createRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant updateRestaurant(String id, Restaurant updatedRestaurant) {
		Restaurant existingRestaurant = restaurantRepository.findById(id).orElse(null);
		if (existingRestaurant != null) {
			updatedRestaurant.setRestaurantId(existingRestaurant.getRestaurantId());
			return restaurantRepository.save(updatedRestaurant);
		}
		return null;
	}

	@Override
	public void deleteRestaurant(String id) {
		restaurantRepository.deleteById(id);
	}
}
