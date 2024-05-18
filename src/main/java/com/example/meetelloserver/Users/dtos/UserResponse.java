package com.example.meetelloserver.Users.dtos;

import lombok.Data;

@Data
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String token;
}
