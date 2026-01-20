package com.annapoorna.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

	
	private String productId;

	private String productName;
	
	private double productPrice;

	private int quantity;

}
