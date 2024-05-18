package com.example.meetelloserver.Users;

import com.example.meetelloserver.Security.JWTService;
import com.example.meetelloserver.Users.dtos.CreateUserReq;
import com.example.meetelloserver.Users.dtos.LoginUserReq;
import com.example.meetelloserver.Users.dtos.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/users/auth")
public class UserController {

        private final UserService userService;
        private final ModelMapper modelMapper;
        private final JWTService jwtService;

        public UserController(
                UserService userService,
                ModelMapper modelMapper,
                JWTService jwtService)
        {
                this.userService=userService;
                this.jwtService = jwtService;
                this.modelMapper= modelMapper;
        }

        @PostMapping("")
        ResponseEntity<UserResponse> createNewUser(@RequestBody CreateUserReq req) throws  UserService.FieldCantBeNullException , UserService.UserAlreadyExistException{
                var savedUser = userService.createUser(req);
                var savedUserURI = URI.create("/users/"+savedUser.getUserId());

                var response = modelMapper.map(savedUser , UserResponse.class);

                response.setToken(jwtService.createJsonWebToken(savedUser.getUserId()));

                return ResponseEntity.created(savedUserURI).body(response);
        }

        @PostMapping("/login")
        ResponseEntity<UserResponse> loginUser(@RequestBody LoginUserReq req) throws  UserService.FieldCantBeNullException , UserService.UserNotFound{
                var savedUser = userService.loginUser(req);
                var savedUserURI = URI.create("/users/"+savedUser.getUserId());

                var response = modelMapper.map(savedUser , UserResponse.class);

                response.setToken(jwtService.createJsonWebToken(savedUser.getUserId()));

                return ResponseEntity.created(savedUserURI).body(response);
        }

}
