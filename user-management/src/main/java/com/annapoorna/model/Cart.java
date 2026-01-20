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
public class Cart {

    @Id
    private Object cartId;

    private User customer;

    private Long total;

    private List<CartItem> cartItem;
}
