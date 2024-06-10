package com.example.meetelloserver.Users;

import com.example.meetelloserver.Security.JWTService;
import com.example.meetelloserver.Users.dtos.CreateUserReq;
import com.example.meetelloserver.Users.dtos.LoginUserReq;
import com.example.meetelloserver.Users.dtos.UserResponse;
import com.mailjet.client.errors.MailjetException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users/auth")
public class UserController {

        private final UserService userService;
        private final ModelMapper modelMapper;
        private final JWTService jwtService;

        public UserController(UserService userService, ModelMapper modelMapper, JWTService jwtService) {
                this.userService = userService;
                this.jwtService = jwtService;
                this.modelMapper = modelMapper;
        }

        @PostMapping("")
        ResponseEntity<UserResponse> createNewUser(@RequestBody CreateUserReq req) throws UserService.FieldCantBeNullException, UserService.UserAlreadyExistException, MailjetException {
                UserEntity savedUser = userService.createUser(req);
                URI savedUserURI = URI.create("/users/" + savedUser.getUserId());
                UserResponse response = modelMapper.map(savedUser, UserResponse.class);
                return ResponseEntity.created(savedUserURI).body(response);
        }

        @GetMapping("/activate")
        ResponseEntity<?> activateUser(@RequestParam String token) {
                try {
                        UserEntity activatedUser = userService.activateUser(token);
                        UserResponse userResponse = modelMapper.map(activatedUser, UserResponse.class);
                        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
                } catch (UserService.UserNotFound e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Activation failed: " + e.getMessage());
                }
        }

        @PostMapping("/login")
        ResponseEntity<UserResponse> loginUser(@RequestBody LoginUserReq req) throws UserService.FieldCantBeNullException, UserService.UserNotFound {
                UserEntity user = userService.loginUser(req);
                UserResponse response = modelMapper.map(user, UserResponse.class);
                response.setToken(jwtService.createJsonWebToken(user.getUserId()));
                return ResponseEntity.ok(response);
        }
}
