package com.spiritcoder.musalalogistics.identity.users.service.components;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.identity.users.api.UserRequest;
import com.spiritcoder.musalalogistics.identity.users.api.UserResponse;
import com.spiritcoder.musalalogistics.identity.users.entity.User;
import com.spiritcoder.musalalogistics.identity.users.repository.UserEntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailValidationProvider implements ValidatorManager {

    private final UserEntityManager userEntityManager;

    @Override
    public UserResponse validate(UserRequest userRequest, UserResponse userResponse) {

        String email = userRequest.getEmail();
        Optional<User> existingUserWithRequestEmail = userEntityManager.getUserByEmail(email);

        existingUserWithRequestEmail.ifPresent(user -> {

            List<String> existingErrorList = userResponse.getErrors();
            existingErrorList.add(AppConstants.EMAIL_ALREADY_EXISTS);
            userResponse.setErrors(existingErrorList);

        });

        return userResponse;
    }
}
