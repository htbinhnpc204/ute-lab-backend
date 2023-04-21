package com.nals.tf7.security.jwt;

import com.nals.tf7.service.v1.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JWTConfigurer
    extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    public void configure(final HttpSecurity http) {
        JWTFilter jwtFilter = new JWTFilter(tokenProvider, redisService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
