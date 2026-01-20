package com.annapoorna.restaurantservice.entity;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class MenuItem {

    @NotBlank(message = "Menu item name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @PositiveOrZero(message = "Price must be a positive or zero value")
    private double price;
}
