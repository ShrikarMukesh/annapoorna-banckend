package com.annapoorna.entity;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItem {

	@NotBlank(message = "Pro	duct ID cannot be blank")
	private String productId;

	@NotBlank(message = "Product name cannot be blank")
	private String productName;

	@Positive(message = "Product price must be positive")
	private double productPrice;

	@Positive(message = "Quantity must be positive")
	private int quantity;

}
