package com.example.meetelloserver.Security;

import com.example.meetelloserver.Users.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final UserService userService;
    private final JWTService jwtService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public AppSecurityConfig(UserService userService , JWTAuthenticationFilter jwtAuthenticationFilter , JWTService jwtService){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService=userService;
        this.jwtService=jwtService;
    }

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
                http
                        .csrf((csrf)-> csrf.disable())
                        .authorizeHttpRequests((authorizeHttpRequest)-> authorizeHttpRequest
                                .requestMatchers(HttpMethod.POST,"/**" ).permitAll()
                                .requestMatchers(HttpMethod.GET,"/**").permitAll()
                                .anyRequest().anonymous())
                        .formLogin(Customizer.withDefaults());
                http.addFilterBefore(jwtAuthenticationFilter , AnonymousAuthenticationFilter.class);

                return http.build();
}
}
