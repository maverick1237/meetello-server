package com.example.meetelloserver.Users.dtos;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter
public class LoginUserReq {
    @NonNull private String email;
    @NonNull private String password;
}