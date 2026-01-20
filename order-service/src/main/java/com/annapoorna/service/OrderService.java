package com.annapoorna.service;

import org.springframework.stereotype.Service;

import com.annapoorna.entity.Order;

import java.util.List;

@Service
public interface OrderService {

	Order createOrder(Order order);

	Order findOrderById(String id);

	List<Order> findAllOrders();

	Order updateOrder(String id, Order updatedOrder);

	void deleteOrder(String id);

	List<Order> findOrdersByCustomerId(String customerId);
}
