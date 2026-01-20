package com.annapoorna.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from User Management Service!";
    }
}
