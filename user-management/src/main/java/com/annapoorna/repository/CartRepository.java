package com.annapoorna.repository;

import com.annapoorna.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, Object> {
}
