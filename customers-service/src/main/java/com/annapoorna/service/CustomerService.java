package com.annapoorna.service;

import com.annapoorna.entity.Customer;


public interface CustomerService {
	
	public Customer register(Customer customer);

	public Customer findByEmail(String email);

	public Customer update(Customer customer);
}
