package com.annapoorna.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.annapoorna.entity.Order;
import com.annapoorna.entity.OrderStatus;
import com.annapoorna.exception.BadRequestException;
import com.annapoorna.exception.ResourceNotFoundException;
import com.annapoorna.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Value("${order.kafka.topic}")
	private String topic;

	private final OrderRepository orderRepository;
	private final AuditLogService auditLogService;

	@Autowired(required = false)
	private KafkaTemplate<String, Order> kafkaTemplate;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, AuditLogService auditLogService) {
		this.orderRepository = orderRepository;
		this.auditLogService = auditLogService;
	}

	@Override
	public Order createOrder(Order order) {
		log.info("Creating new order for customer id: {}", order.getCustomerId());

		// Business logic: calculating total cost
		double totalCost = order.getItems().stream()
				.mapToDouble(item -> item.getProductPrice() * item.getQuantity())
				.sum();
		order.setTotalCost(totalCost);

		// Basic validation: total cost should be positive
		if (order.getTotalCost() <= 0) {
			throw new BadRequestException("Total cost must be positive.");
		}

		// Basic validation: order status should be 'PLACED' for new orders
		if (order.getOrderStatus() != OrderStatus.PLACED) {
			throw new BadRequestException("Order status for new orders must be 'PLACED'.");
		}

		// Basic validation: there should be at least one item in the order
		if (order.getItems().isEmpty()) {
			throw new BadRequestException("Order should have at least one item.");
		}

		// Now, save the order in the database
		Order response = orderRepository.save(order);
		sendOrderToKafka(response);

		// Audit log for successful order creation
		auditLogService.logSuccess(
				"CREATE",
				"Order",
				response.getOrderId() != null ? response.getOrderId().toString() : null,
				order.getCustomerId(),
				null,
				response,
				"Order created successfully",
				null);

		// Return the saved entity (do not save again)
		return response;
	}

	private void sendOrderToKafka(Order response) {
		if (response != null && kafkaTemplate != null) {
			try {
				kafkaTemplate.send(topic, response);
				log.info("Published order with id {} to topic {}", response.getOrderId(), topic);
			} catch (Exception e) {
				log.error("Failed to publish order {} to kafka topic {}: {}", response.getOrderId(), topic,
						e.getMessage(), e);
			}
		} else {
			log.debug("Skipping kafka publish because response or kafkaTemplate is null");
		}
	}

	@Override
	public List<Order> findAllOrders() {
		log.info("Fetching all orders");
		List<Order> orders = orderRepository.findAll();

		auditLogService.logSuccess(
				"READ",
				"Order",
				null,
				null,
				null,
				null,
				"Fetched all orders. Count: " + orders.size(),
				null);

		return orders;
	}

	@Override
	public Order findOrderById(String id) {
		log.info("Fetching order with id: {}", id);
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> {
					log.error("Order not found with id: {}", id);
					return new ResourceNotFoundException("Order not found with id: " + id);
				});

		auditLogService.logSuccess(
				"READ",
				"Order",
				id,
				order.getCustomerId(),
				null,
				null,
				"Order retrieved successfully",
				null);

		return order;
	}

	@Override
	public List<Order> findOrdersByCustomerId(String customerId) {
		log.info("Fetching orders for customer id: {}", customerId);
		List<Order> orders = orderRepository.findByCustomerId(customerId);

		auditLogService.logSuccess(
				"READ",
				"Order",
				null,
				customerId,
				null,
				null,
				"Fetched orders for customer. Count: " + orders.size(),
				null);

		return orders;
	}

	@Override
	public Order updateOrder(String id, Order updatedOrder) {
		log.info("Updating order with id: {}", id);

		return orderRepository.findById(id)
				.map(order -> {
					// Capture old state for audit (store key fields before update)
					var oldStatus = order.getOrderStatus();
					Double oldTotalCost = order.getTotalCost();

					// Update the order fields here:
					order.setCustomerId(updatedOrder.getCustomerId());
					order.setItems(updatedOrder.getItems());
					order.setOrderStatus(updatedOrder.getOrderStatus());
					order.setPayment(updatedOrder.getPayment());
					order.setDeliveryDetailsId(updatedOrder.getDeliveryDetailsId());

					// Recalculate the total cost in case the items have changed
					double totalCost = order.getItems().stream()
							.mapToDouble(item -> item.getProductPrice() * item.getQuantity())
							.sum();
					order.setTotalCost(totalCost);

					Order savedOrder = orderRepository.save(order);

					// Audit log for successful update
					auditLogService.logSuccess(
							"UPDATE",
							"Order",
							id,
							updatedOrder.getCustomerId(),
							"Previous status: " + oldStatus + ", Previous total: " + oldTotalCost,
							savedOrder,
							"Order updated successfully",
							null);

					return savedOrder;
				})
				.orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));
	}

	@Override
	public void deleteOrder(String id) {
		log.info("Deleting order with id: {}", id);

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> {
					log.error("Order not found with id: {}", id);
					return new ResourceNotFoundException("Order not found with id: " + id);
				});

		orderRepository.deleteById(id);

		// Audit log for successful deletion
		auditLogService.logSuccess(
				"DELETE",
				"Order",
				id,
				order.getCustomerId(),
				order,
				null,
				"Order deleted successfully",
				null);
	}
}
