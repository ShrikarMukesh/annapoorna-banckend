package com.annapoorna.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	
	@NotBlank(message = "Payment ID cannot be blank")
	private String paymentId;
	
	@NotBlank(message = "Order ID cannot be blank")
	private String orderId;
	
	@Positive(message = "Payment amount must be positive")
	private double amount;

	@NotNull(message = "Payment status cannot be null")
	private PaymentStatus paymentStatus;

}
