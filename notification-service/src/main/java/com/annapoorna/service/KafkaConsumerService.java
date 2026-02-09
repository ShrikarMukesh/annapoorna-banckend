package com.annapoorna.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.annapoorna.entity.Order;
import com.annapoorna.notification.service.AuditLogService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService {

	@Value("${order.kafka.topic}")
	private String topic;

	private final JavaMailSender javaMailSender;
	private final AuditLogService auditLogService;

	@Autowired
	public KafkaConsumerService(JavaMailSender javaMailSender, AuditLogService auditLogService) {
		this.javaMailSender = javaMailSender;
		this.auditLogService = auditLogService;
	}

	@KafkaListener(topics = "${order.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(Order order) {
		log.info("Received order from Kafka: {}", order.getOrderId());

		try {
			processOrder(order);

			// Log successful processing
			auditLogService.logSuccess(
					"ORDER_RECEIVED",
					"Order",
					order.getOrderId(),
					order.getCustomerId(),
					null,
					order,
					"Order received and processed from Kafka topic: " + topic,
					null);
		} catch (Exception e) {
			log.error("Error processing order: {}", e.getMessage(), e);

			// Log error
			auditLogService.logError(
					"ORDER_PROCESSING_FAILED",
					"Order",
					order.getOrderId(),
					order.getCustomerId(),
					500,
					e.getMessage(),
					null,
					null);
		}
	}

	private void processOrder(Order order) {
		// Process the order
		log.info("Order details: " + order);

		// Once processing is complete, send an email.
		// SimpleMailMessage msg = new SimpleMailMessage();
		// msg.setTo("mukesh.shrikar10@gmail.com");
		// msg.setSubject("Order processed");
		// msg.setText("Your order " + order.getOrderId() + " has been processed.");
		// javaMailSender.send(msg);
	}
}
