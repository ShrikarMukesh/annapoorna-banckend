package com.annapoorna.service.impl;

import com.annapoorna.config.JwtProvider;
import com.annapoorna.constants.JwtConstant;
import com.annapoorna.exception.ResourceNotFoundException;
import com.annapoorna.model.User;
import com.annapoorna.repository.UserRepository;
import com.annapoorna.service.AuditLogService;
import com.annapoorna.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtConstant jwtConstant;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        log.info("Finding user by JWT token");
        try {
            String email = jwtProvider.getEmailFromToken(jwt);
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new ResourceNotFoundException("User not found for the provided token");
            }

            auditLogService.logSuccess(
                    "READ",
                    "User",
                    user.getUserId(),
                    user.getUserId(),
                    null,
                    null,
                    "User retrieved by JWT token",
                    null);

            return user;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error finding user by JWT token: {}", e.getMessage());
            throw new Exception("Invalid token", e);
        }
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        log.info("Finding user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        auditLogService.logSuccess(
                "READ",
                "User",
                user.getUserId(),
                user.getUserId(),
                null,
                null,
                "User retrieved by email",
                null);

        return user;
    }
}
