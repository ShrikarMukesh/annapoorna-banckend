package com.annapoorna.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    private Long id;

    private User owner;

    private String name;

    private String description;

    private String cuisineType;

    private Address address;

    private ContactInformation contactInformation;

    private String openingHours;

    private List<Order> orders = new ArrayList<>();

    private List<String> images;

    private LocalDateTime registrationDate;

    private boolean open;

    private List<Food> foods = new ArrayList<>();

}
