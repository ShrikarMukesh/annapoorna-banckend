package com.annapoorna.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

	private String orderId;
	private String customerId;
	private String restaurantId;
	private List<OrderItem> items;
	private Address address;
	private OrderStatus orderStatus;
	private LocalDateTime orderTime;
	private LocalDateTime deliveryTime;
	private Double totalCost;
	private Payment payment;
	private String deliveryDetailsId;
	private long packagingCharge;
	private long platformFee;
	private long deliveryPattnerFee;
	private long tax;

	@Data
	@AllArgsConstructor
	public static class OrderItem {

		private String productId;
		private String productName;
		private double productPrice;
		private int quantity;
	}

	@Data
	@AllArgsConstructor
	public static class Address {

		private String street;
		private String city;
		private String state;
		private String country;
		private String zipCode;
	}

	@Data
	@AllArgsConstructor
	public static class Payment {
		private String paymentId;
		private String orderId;
		private double amount;
		private PaymentStatus status;
	}
	enum OrderStatus{
		PLACED,
		SHIPPED,
		DELIVERED,
		CANCELLED,
		ORDERED
	}
	enum PaymentStatus {
		PENDING,
		PAID,
		CONFIRMED,
		FAILED
	}
}
