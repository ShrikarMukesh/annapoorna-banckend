package com.annapoorna.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.annapoorna.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	Optional<Customer> findByUserName(String username);
    Customer findByEmail(String email);
}
