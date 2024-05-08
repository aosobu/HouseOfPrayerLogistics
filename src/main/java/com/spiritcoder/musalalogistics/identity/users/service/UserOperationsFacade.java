package com.spiritcoder.musalalogistics.identity.users.service;

import com.spiritcoder.musalalogistics.identity.users.api.UserRequest;
import com.spiritcoder.musalalogistics.identity.users.api.UserResponse;
import com.spiritcoder.musalalogistics.identity.users.service.components.UserSaveComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserOperationsFacade {

    private final UserSaveComponent userSaveComponent;

    public UserResponse saveUser(UserRequest userRequest){
        return userSaveComponent.saveUser(userRequest);
    }
}
