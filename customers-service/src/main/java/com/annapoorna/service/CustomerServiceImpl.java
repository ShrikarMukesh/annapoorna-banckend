package com.annapoorna.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.annapoorna.dto.CustomerResponseDTO;
import com.annapoorna.entity.Customer;
import com.annapoorna.repository.CustomerRepository;

@Service
public class CustomerServiceImpl {

	private final CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public CustomerResponseDTO register(Customer customer) {
		
		//Password encoding
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		customer =  customerRepository.save(customer);
		
		return CustomerResponseDTO.builder()
				.customerId(customer.getCustomerId())
				.userName(customer.getUserName())
				.firstName(customer.getFirstName())
				.lastName(customer.getLastName())
				.email(customer.getEmail())
				.address(customer.getAddress())
				.phoneNumber(customer.getPhoneNumber())
				.build();
	}

	public List<Customer> getAllCustomer( ) {
		return customerRepository.findAll();
	}

	public Customer findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	public Customer update(Customer customer) {
		return customerRepository.save(customer);
	}
}
