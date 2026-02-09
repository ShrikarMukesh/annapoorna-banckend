package com.annapoorna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.annapoorna.dto.CustomerResponseDTO;
import com.annapoorna.entity.Customer;
import com.annapoorna.exception.ResourceNotFoundException;
import com.annapoorna.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl {

	private final CustomerRepository customerRepository;
	private final AuditLogService auditLogService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository, AuditLogService auditLogService) {
		this.customerRepository = customerRepository;
		this.auditLogService = auditLogService;
	}

	public CustomerResponseDTO register(Customer customer) {
		log.info("Registering new customer with email: {}", customer.getEmail());

		// Password encoding
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		customer = customerRepository.save(customer);

		CustomerResponseDTO response = CustomerResponseDTO.builder()
				.customerId(customer.getCustomerId())
				.userName(customer.getUserName())
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.email(customer.getEmail())
				.address(customer.getAddress())
				.phoneNumber(customer.getPhoneNumber())
				.build();

		// Audit log for successful registration
		auditLogService.logSuccess(
				"CREATE",
				"Customer",
				customer.getCustomerId(),
				customer.getCustomerId(),
				null,
				response,
				"Customer registered successfully",
				null);

		return response;
	}

	public List<Customer> getAllCustomer() {
		log.info("Fetching all customers");
		List<Customer> customers = customerRepository.findAll();

		auditLogService.logSuccess(
				"READ",
				"Customer",
				null,
				null,
				null,
				null,
				"Fetched all customers. Count: " + customers.size(),
				null);

		return customers;
	}

	public Customer findByEmail(String email) {
		log.info("Finding customer by email: {}", email);
		Customer customer = customerRepository.findByEmail(email);

		if (customer != null) {
			auditLogService.logSuccess(
					"READ",
					"Customer",
					customer.getCustomerId(),
					customer.getCustomerId(),
					null,
					null,
					"Customer found by email",
					null);
		}

		return customer;
	}

	public Customer findById(String customerId) {
		log.info("Finding customer by id: {}", customerId);
		Optional<Customer> customer = customerRepository.findById(customerId);

		if (customer.isPresent()) {
			auditLogService.logSuccess(
					"READ",
					"Customer",
					customerId,
					customerId,
					null,
					null,
					"Customer retrieved successfully",
					null);
			return customer.get();
		}

		throw new ResourceNotFoundException("Customer not found with id: " + customerId);
	}

	public Customer update(Customer customer) {
		log.info("Updating customer with id: {}", customer.getCustomerId());

		// Get the old customer for audit
		Optional<Customer> oldCustomer = customerRepository.findById(customer.getCustomerId());
		if (oldCustomer.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found with id: " + customer.getCustomerId());
		}

		Customer updatedCustomer = customerRepository.save(customer);

		// Audit log for successful update
		auditLogService.logSuccess(
				"UPDATE",
				"Customer",
				customer.getCustomerId(),
				customer.getCustomerId(),
				oldCustomer.get(),
				updatedCustomer,
				"Customer updated successfully",
				null);

		return updatedCustomer;
	}
}
