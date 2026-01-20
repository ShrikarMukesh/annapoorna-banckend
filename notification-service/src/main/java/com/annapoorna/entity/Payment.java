package com.annapoorna.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	
	private String paymentId;
	
	private String orderId;
	
	private double amount;

	private PaymentStatus paymentStatus;

}
