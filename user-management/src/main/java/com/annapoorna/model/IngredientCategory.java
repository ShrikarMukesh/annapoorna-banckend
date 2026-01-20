package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IngredientCategory {

    private Long id;

    private String name;

    private Restaurant restaurant;

    private List<IngredientsItem> ingredients = new ArrayList<>();
}
