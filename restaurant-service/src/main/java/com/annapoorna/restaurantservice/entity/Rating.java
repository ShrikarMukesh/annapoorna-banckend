package com.annapoorna.restaurantservice.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Rating {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Min(value = 0, message = "Rating must be between 0 and 5")
    @Max(value = 5, message = "Rating must be between 0 and 5")
    private int rating;

    private String review;

    private LocalDateTime createdAt;

    // Constructor
    public Rating() {
        this.createdAt = LocalDateTime.now();
    }

    public int getRating() {
        return rating;
    }
}
