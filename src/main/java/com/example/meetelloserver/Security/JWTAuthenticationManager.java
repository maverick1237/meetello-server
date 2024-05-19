package com.example.meetelloserver.Security;

import com.example.meetelloserver.Users.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationManager  implements AuthenticationManager {
    private final JWTService jwtService;
    private final UserService userService;

    public JWTAuthenticationManager(UserService userService , JWTService jwtService){
        this.jwtService = jwtService;
        this.userService = userService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof  JWTAuthentication jwtAuthentication){
            var jwt = jwtAuthentication.getCredentials();
            if (jwt == null || ((String) jwt).isEmpty()) {
                throw new IllegalArgumentException("JWT token is missing");
            }
            var userId = jwtService.retrieveUserId((String) jwt);

            jwtAuthentication.userEntity = userService.findUserById(userId);
            jwtAuthentication.setAuthenticated(true);
            return  jwtAuthentication;
        }

        throw new IllegalAccessError("can't authenticate with null jwt authentication");
    }
}
