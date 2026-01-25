package com.annapoorna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreatedEvent {
    private String userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;
    private String password;
}
