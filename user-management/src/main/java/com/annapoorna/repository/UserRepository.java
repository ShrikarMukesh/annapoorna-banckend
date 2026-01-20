package com.annapoorna.repository;

import com.annapoorna.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Object> {
    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
}
