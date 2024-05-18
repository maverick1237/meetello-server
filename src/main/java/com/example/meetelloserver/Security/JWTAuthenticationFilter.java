package com.example.meetelloserver.Security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationFilter extends AuthenticationFilter {

private JWTAuthenticationManager jwtAuthenticationManager;
public  JWTAuthenticationFilter(JWTAuthenticationManager jwtAuthenticationManager){
    super(jwtAuthenticationManager  , new JWTAuthenticationConvertor());

    this.setSuccessHandler(((request, response, authentication) -> {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }));
}
}
