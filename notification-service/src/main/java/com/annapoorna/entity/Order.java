package com.annapoorna.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
	
    private String orderId;
    private String customerId;
	private String restaurantID;
	private Address address;
    private List<OrderItem> items;
    private double totalCost;
    private OrderStatus orderStatus;
    private Payment payment;
    private String deliveryDetailsId;
    private long packagingCharge;
    private long platformFee;
    private long deliveryPattnerFee;
    private long tax;

}
