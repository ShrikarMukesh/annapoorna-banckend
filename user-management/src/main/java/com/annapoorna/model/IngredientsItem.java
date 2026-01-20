package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsItem {

    @Id
    private Long ingredientId;

    private String name;

    private IngredientCategory category;

    private  Restaurant restaurant;

    private boolean isStock = true;
}
