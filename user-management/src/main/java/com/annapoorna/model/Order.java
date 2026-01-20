package com.annapoorna.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Order {

    private String orderId;

    private String customerId;

    private Restaurant restaurant;

    private Long totalCost;

    private String orderStatus;

    private Date createdAt;

    private Address deliveryAddress;

    private List<OrderItems> items;

    private int totalItems;

    private int totalPrice;
}
