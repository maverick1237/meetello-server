package com.example.meetelloserver.Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class JWTAuthenticationConvertor implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        var authenticationHeader = request.getHeader("Authorization");
        if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ") ){
            return  null;
        }
        var jwt = authenticationHeader.substring(7);
        return new JWTAuthentication(jwt);
    }
}
