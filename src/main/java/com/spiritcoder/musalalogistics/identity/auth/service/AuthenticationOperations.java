package com.spiritcoder.musalalogistics.identity.auth.service;

import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationRequest;
import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationOperations {

    private final AuthenticationComponent authenticationComponent;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        return authenticationComponent.authenticate(authenticationRequest);
    }
}
