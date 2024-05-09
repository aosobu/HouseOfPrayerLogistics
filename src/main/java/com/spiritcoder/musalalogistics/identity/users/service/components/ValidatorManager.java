package com.spiritcoder.musalalogistics.identity.users.service.components;

import com.spiritcoder.musalalogistics.identity.users.api.UserRequest;
import com.spiritcoder.musalalogistics.identity.users.api.UserResponse;

public interface ValidatorManager {

    UserResponse validate(UserRequest userRequest, UserResponse userResponse);
}
