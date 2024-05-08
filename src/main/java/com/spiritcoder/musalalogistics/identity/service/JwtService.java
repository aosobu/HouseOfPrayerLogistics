package com.spiritcoder.musalalogistics.identity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtOperations jwtOperations;

    @Autowired
    public JwtService(JwtOperations jwtServiceOperations) {
        this.jwtOperations = jwtServiceOperations;
    }

    public String getUsername(String jwt) {
        return jwtOperations.extractUsername(jwt);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return jwtOperations.isTokenValid(token, userDetails);
    }

    public String getToken(UserDetails userDetails) {
        return jwtOperations.generateToken(userDetails);
    }
}
