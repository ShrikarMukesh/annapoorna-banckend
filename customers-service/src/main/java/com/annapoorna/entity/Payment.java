package com.annapoorna.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

	private String id;

	private String userId;
	private BigDecimal amount;
	private PaymentStatus paymentStatus; // e.g. "completed", "refunded"

}
