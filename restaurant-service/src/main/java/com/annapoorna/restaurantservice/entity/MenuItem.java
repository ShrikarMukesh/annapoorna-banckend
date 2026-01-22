package com.annapoorna.restaurantservice.entity;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class MenuItem {

    private String id;

    @NotBlank(message = "Menu item name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @PositiveOrZero(message = "Price must be a positive or zero value")
    private double price;

    private String imageId;

    private boolean isVeg;

    private double rating;

    private int ratingCount;

    private boolean isCustomisable;

    private String category;

    private boolean available;

    private Discount discount;

    @Data
    public static class Discount {
        private double amount;
        private String label;
    }
}
