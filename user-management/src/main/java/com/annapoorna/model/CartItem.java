package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class CartItem {

    @Id
    private Long id;

    private Cart cart;

    private Food food;

    private int quantity;

    private List<String> ingredients;

    private Long totalPrice;
}
