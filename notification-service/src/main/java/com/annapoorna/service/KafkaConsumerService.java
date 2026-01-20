package com.annapoorna.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.annapoorna.entity.Order;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService {

	@Value("${order.kafka.topic}")
	private String topic;


	JavaMailSender javaMailSender;

	public KafkaConsumerService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@KafkaListener(topics = "${order.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(Order order) {
		processOrder(order);
	}

	private void processOrder(Order order) {
		// Process the order
		log.info("Order details: " + order);

		// Once processing is complete, send an email.
		//		SimpleMailMessage msg = new SimpleMailMessage();
		//		msg.setTo("mukesh.shrikar10@gmail.com");
		//		msg.setSubject("Order processed");
		//		msg.setText("Your order " + order.getOrderId() + " has been processed.");
		//		javaMailSender.send(msg);
	}

	//	@KafkaListener(topics = "${order.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	//	public void consume(Order order) {
	//		//public void consume(OrderDTO orderDTO) {
	//
	//		// Here, orderDetails is a JSON string. Convert; it back to an Order object.
	//		ObjectMapper objectMapper = new ObjectMapper();
	//		//OrderDTO orderDTO = new OrderDTO();
	//		try {
	//			order = objectMapper.readValue(order, Order.class);
	//		} catch (JsonProcessingException e) {
	//			e.printStackTrace();
	//		}
	//
	//		// Process the order...
	//		log.info("Order details "+ order);
	//
	//		// Once processing is complete, send an email.
	//		SimpleMailMessage msg = new SimpleMailMessage();
	//		msg.setTo("mukesh.shrikar10@gmail.com");
	//
	//		msg.setSubject("Order processed");
	//		msg.setText("Your order " + order.getOrderId() + " has been processed.");
	//
	//		javaMailSender.send(msg);
	//	}
}
