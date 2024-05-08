package com.spiritcoder.musalalogistics.identity.auth.service;


import com.spiritcoder.musalalogistics.commons.wrappers.BeanWrapper;
import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationRequest;
import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationResponse;
import com.spiritcoder.musalalogistics.identity.service.JwtService;
import com.spiritcoder.musalalogistics.identity.users.repository.UserEntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationComponent {

    private final AuthenticationManager authenticationManager;

    private final UserEntityManager userEntityManager;

    private final JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String jwtToken = "";
        authenticationManager.authenticate(BeanWrapper.getAuthenticationBean(request));
        var user = userEntityManager.getUserByEmail(request.getEmail());
        if(user.isPresent()){
            jwtToken = jwtService.getToken(user.get());
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
