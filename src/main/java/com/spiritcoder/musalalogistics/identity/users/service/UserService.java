package com.spiritcoder.musalalogistics.identity.users.service;

import com.spiritcoder.musalalogistics.identity.users.api.UserRequest;
import com.spiritcoder.musalalogistics.identity.users.api.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserOperationsFacade userOperations;

    public UserResponse registerUser(UserRequest userRequest){
        return userOperations.saveUser(userRequest);
    }
}
