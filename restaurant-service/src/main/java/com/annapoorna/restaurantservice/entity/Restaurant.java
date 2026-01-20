package com.annapoorna.restaurantservice.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Document(collection = "restaurants")
public class Restaurant {

	@Id
	private String restaurantId;

	private String cloudinaryImageId;

	@NotBlank(message = "Restaurant name is required")
	private String name;

	@NotBlank(message = "Description is required")
	private String description;

	@NotNull(message = "Cuisine list cannot be null")
	@Valid
	private List<String> cuisines;

	@NotNull(message = "Address cannot be null")
	@Valid
	private Address address;

	@NotNull(message = "Contact information cannot be null")
	@Valid
	private Contact contact;

	@NotNull(message = "Menu items list cannot be null")
	@Valid
	private List<MenuItem> menu;

	@NotNull(message = "Ratings list cannot be null")
	@Valid
	private List<Rating> ratings;

	@PositiveOrZero(message = "Average rating must be a positive or zero value")
	private double averageRating;

	@NotNull(message = "Created timestamp cannot be null")
	private LocalDateTime createdAt;

	@NotNull(message = "Updated timestamp cannot be null")
	private LocalDateTime updatedAt;

	// Constructor
	public Restaurant() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	// Method to update average rating
	public void updateAverageRating() {
		if (!ratings.isEmpty()) {
			this.averageRating = ratings.stream()
					.mapToDouble(Rating::getRating)
					.average()
					.orElse(0.0);
		} else {
			this.averageRating = 0.0;
		}
		this.updatedAt = LocalDateTime.now(); // Update the timestamp when ratings change
	}
}

