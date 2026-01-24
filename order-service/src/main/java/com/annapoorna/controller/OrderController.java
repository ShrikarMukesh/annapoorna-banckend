package com.annapoorna.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.annapoorna.entity.Order;
import com.annapoorna.service.OrderServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import org.springdoc.api.annotations.Operation;
//import org.springdoc.api.annotations.Parameter;
//import org.springdoc.api.annotations.responses.ApiResponse;
//import org.springdoc.api.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin("*")
public class OrderController {

	private final OrderServiceImpl orderServiceImpl;

	public OrderController(OrderServiceImpl orderServiceImpl) {
		this.orderServiceImpl = orderServiceImpl;
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
		return ResponseEntity.ok(orderServiceImpl.createOrder(order));
	}

	@Operation(summary = "Get an order by its id")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Found the order"),
			@ApiResponse(responseCode = "404", description = "Order not found") 
	})
	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable String id) {
		return ResponseEntity.ok(orderServiceImpl.findOrderById(id));
	}

	@Operation(summary = "Get all orders")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Found the orders"),
			@ApiResponse(responseCode = "404", description = "Orders not found") 
	})
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		List<Order> orders = orderServiceImpl.findAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable String id, @Valid @RequestBody Order updatedOrder) {
		Order order = orderServiceImpl.updateOrder(id, updatedOrder);
		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
		orderServiceImpl.deleteOrder(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable String customerId) {
		List<Order> orders = orderServiceImpl.findOrdersByCustomerId(customerId);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

}
