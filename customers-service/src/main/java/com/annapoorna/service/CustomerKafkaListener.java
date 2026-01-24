package com.annapoorna.service;

import com.annapoorna.dto.UserCreatedEvent;
import com.annapoorna.entity.Customer;
import com.annapoorna.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerKafkaListener {

    @Autowired
    private CustomerRepository customerRepository;

    @KafkaListener(topics = "user-created-topic", groupId = "customers-group")
    public void consumeUserCreatedEvent(UserCreatedEvent event) {
        System.out.println("Received UserCreatedEvent: " + event);

        // Check if customer already exists
        if (event.getEmail() != null && customerRepository.findByEmail(event.getEmail()) != null) {
            System.out.println("Customer with email " + event.getEmail() + " already exists.");
            return;
        }

        Customer customer = new Customer();
        customer.setCustomerId(event.getUserId()); // Sync ID if possible, or let Mongo generate one and store userId as reference
        customer.setEmail(event.getEmail());
        customer.setFirstName(event.getFullName()); // Assuming full name maps to first name for now, or split it
        customer.setPhoneNumber(event.getPhoneNumber());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        // Handle name splitting if needed
        if (event.getFullName() != null) {
            String[] names = event.getFullName().split(" ", 2);
            customer.setFirstName(names[0]);
            if (names.length > 1) {
                customer.setLastName(names[1]);
            }
        }

        customerRepository.save(customer);
        System.out.println("Customer created from Kafka event: " + customer);
    }
}
