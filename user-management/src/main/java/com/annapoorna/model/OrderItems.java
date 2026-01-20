package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItems {

    private Long orderItemId;
    private Food food;
    private int quantity;
    private Long totalPrice;
    private List<String> ingredients;
}
