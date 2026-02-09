package com.annapoorna.restaurantservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.annapoorna.restaurantservice.entity.Restaurant;
import com.annapoorna.restaurantservice.exception.ResourceNotFoundException;
import com.annapoorna.restaurantservice.repository.RestaurantRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final AuditLogService auditLogService;

	@Autowired
	public RestaurantServiceImpl(RestaurantRepository restaurantRepository, AuditLogService auditLogService) {
		this.restaurantRepository = restaurantRepository;
		this.auditLogService = auditLogService;
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		log.info("Fetching all restaurants");
		List<Restaurant> restaurants = restaurantRepository.findAll();

		auditLogService.logSuccess(
				"READ",
				"Restaurant",
				null,
				null,
				null,
				null,
				"Fetched all restaurants. Count: " + restaurants.size(),
				null);

		return restaurants;
	}

	@Override
	public Restaurant getRestaurantById(String id) {
		log.info("Fetching restaurant with id: {}", id);
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> {
					log.error("Restaurant not found with id: {}", id);
					return new ResourceNotFoundException("Restaurant not found with id: " + id);
				});

		auditLogService.logSuccess(
				"READ",
				"Restaurant",
				id,
				null,
				null,
				null,
				"Restaurant retrieved successfully",
				null);

		return restaurant;
	}

	@Override
	public Restaurant createRestaurant(Restaurant restaurant) {
		log.info("Creating new restaurant: {}", restaurant.getName());
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);

		auditLogService.logSuccess(
				"CREATE",
				"Restaurant",
				savedRestaurant.getRestaurantId(),
				null,
				null,
				savedRestaurant,
				"Restaurant created successfully",
				null);

		return savedRestaurant;
	}

	@Override
	public List<Restaurant> createRestaurants(List<Restaurant> restaurants) {
		log.info("Creating {} restaurants", restaurants.size());
		List<Restaurant> savedRestaurants = restaurantRepository.saveAll(restaurants);

		auditLogService.logSuccess(
				"CREATE_BATCH",
				"Restaurant",
				null,
				null,
				null,
				savedRestaurants,
				"Created " + savedRestaurants.size() + " restaurants",
				null);

		return savedRestaurants;
	}

	@Override
	public Restaurant updateRestaurant(String id, Restaurant updatedRestaurant) {
		log.info("Updating restaurant with id: {}", id);

		Restaurant existingRestaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> {
					log.error("Restaurant not found with id: {}", id);
					return new ResourceNotFoundException("Restaurant not found with id: " + id);
				});

		// Capture old state for audit
		String oldRestaurantName = existingRestaurant.getName();

		updatedRestaurant.setRestaurantId(existingRestaurant.getRestaurantId());
		Restaurant savedRestaurant = restaurantRepository.save(updatedRestaurant);

		auditLogService.logSuccess(
				"UPDATE",
				"Restaurant",
				id,
				null,
				existingRestaurant,
				savedRestaurant,
				"Restaurant updated from '" + oldRestaurantName + "' to '" + savedRestaurant.getName() + "'",
				null);

		return savedRestaurant;
	}

	@Override
	public void deleteRestaurant(String id) {
		log.info("Deleting restaurant with id: {}", id);

		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(() -> {
					log.error("Restaurant not found with id: {}", id);
					return new ResourceNotFoundException("Restaurant not found with id: " + id);
				});

		restaurantRepository.deleteById(id);

		auditLogService.logSuccess(
				"DELETE",
				"Restaurant",
				id,
				null,
				restaurant,
				null,
				"Restaurant '" + restaurant.getName() + "' deleted successfully",
				null);
	}
}
