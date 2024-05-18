package com.example.meetelloserver.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt}")
    String JWT_KEY ;
    private Algorithm algorithm ;

    @PostConstruct
    public void init(){
        algorithm = Algorithm.HMAC256(JWT_KEY);
    }
    public String createJsonWebToken(String userID){
        return JWT.create()
                .withSubject(userID)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public String retrieveUserId (String jwtString){
        var decodedSJWT = JWT.decode(jwtString);
        return decodedSJWT.getSubject();
    }
}
