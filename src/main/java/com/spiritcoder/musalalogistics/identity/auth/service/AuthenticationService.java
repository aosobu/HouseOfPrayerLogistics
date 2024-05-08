package com.spiritcoder.musalalogistics.identity.auth.service;

import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationRequest;
import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationOperations authenticationOperations;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        return authenticationOperations.authenticate(authenticationRequest);
    }
}
