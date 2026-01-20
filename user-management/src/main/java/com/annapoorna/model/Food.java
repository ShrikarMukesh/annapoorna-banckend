package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    @Id
    private Long id;

    private String name;

    private String description;

    private Long price;

    private Category foodCategory;

    private List<String> images;

    private boolean available;

    private Restaurant restaurant;

    private boolean isVeg;

    private boolean isSeasonal;

    private List<IngredientsItem>  ingredientItems = new ArrayList<>();

    private Date creationDate;
}
