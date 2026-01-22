package com.annapoorna.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
	
    private String orderId;

    private String customerId;
	@NotBlank(message = "Restaurant ID cannot be blank")
	private String restaurantID;
	private Address address;
    @Valid
    @NotNull(message = "Order items cannot be null")
    private List<OrderItem> items;
    private double totalCost;
    private OrderStatus orderStatus;
    @Valid
    @NotNull(message = "Payment cannot be null")
    private Payment payment;
    private String deliveryDetailsId;
    private long packagingCharge;
    private long platformFee;
    private long deliveryPattnerFee;
    private long tax;

}
