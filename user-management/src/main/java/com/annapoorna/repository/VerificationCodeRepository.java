package com.annapoorna.repository;

import com.annapoorna.model.VerificationCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationCodeRepository extends MongoRepository<VerificationCode, String> {
    VerificationCode findByPhoneNumber(String phoneNumber);
}
