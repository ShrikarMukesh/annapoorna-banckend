package com.annapoorna.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

	private String orderId;
	private String customerId;
	@NotBlank(message = "Restaurant ID cannot be blank")
	private String restaurantId;
	@Valid
	@NotNull(message = "Order items cannot be null")
	private List<OrderItem> items;
	private Address address;
	private OrderStatus orderStatus;
	private LocalDateTime orderTime;
	private LocalDateTime deliveryTime;
	private Double totalCost;
	@Valid
	@NotNull(message = "Payment cannot be null")
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
		@NotBlank(message = "Payment ID cannot be blank")
		private String paymentId;

		@NotBlank(message = "Order ID cannot be blank")
		private String orderId;

		@Positive(message = "Payment amount must be positive")
		private double amount;

		@NotNull(message = "Payment status cannot be null")
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
