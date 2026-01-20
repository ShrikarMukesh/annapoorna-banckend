package com.annapoorna.service.impl;

import com.annapoorna.config.JwtProvider;
import com.annapoorna.constants.JwtConstant;
import com.annapoorna.model.User;
import com.annapoorna.repository.UserRepository;
import com.annapoorna.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtConstant jwtConstant;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        try {
            String email = jwtProvider.getEmailFromToken(jwt);
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("User not found");
            }
            return user;
        } catch (Exception e) {
            throw new Exception("Invalid token", e);
        }
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }
}
