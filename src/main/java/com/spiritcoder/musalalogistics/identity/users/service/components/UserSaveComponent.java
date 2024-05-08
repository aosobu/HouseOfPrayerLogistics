package com.spiritcoder.musalalogistics.identity.users.service.components;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.wrappers.BeanWrapper;
import com.spiritcoder.musalalogistics.identity.users.api.UserRequest;
import com.spiritcoder.musalalogistics.identity.users.api.UserResponse;
import com.spiritcoder.musalalogistics.identity.users.dto.UserDTO;
import com.spiritcoder.musalalogistics.identity.users.entity.User;
import com.spiritcoder.musalalogistics.identity.users.enums.Role;
import com.spiritcoder.musalalogistics.identity.users.repository.UserEntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSaveComponent {

    private final UserEntityManager userEntityManager;

    private final List<ValidatorManager> validatorProviders;


    public UserResponse saveUser(UserRequest userRequest){
        UserResponse [] validationResponseContainer = new UserResponse[1];
        UserResponse userResponse = new UserResponse();

        validationResponseContainer[0] = userResponse;

        validatorProviders
                .forEach( validatorProvider ->{
                            validationResponseContainer[0] = validatorProvider.validate(userRequest, validationResponseContainer[0]);
                });


        UserResponse validationResponse = validationResponseContainer[0];


        if(!validationResponse.isEmptyErrorList()){

            validationResponse.setMessage(AppConstants.USER_REGISTER_FAILURE_MESSAGE);
            return validationResponse;
        }


        if(validationResponse.isEmptyErrorList()){

            String username = extractUserName(userRequest.getEmail());;

            Optional<User> savedUser = userEntityManager.saveUser(userRequest.getEmail(), username, BeanWrapper.getPasswordEncoder().encode(userRequest.getPassword()),
                    Role.USER, true, LocalDateTime.now(), AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

            savedUser.ifPresent(user -> updateUserResponse(user, userResponse));
        }

        return userResponse;
    }

    private void updateUserResponse(User savedUser, UserResponse userResponse) {

        if(savedUser != null){
            userResponse.setMessage(AppConstants.USER_REGISTER_SUCCESS_MESSAGE);
            userResponse.setUserDTO(buildUserDTO(savedUser));
        }else{
            userResponse.setMessage(AppConstants.USER_REGISTER_FAILURE_MESSAGE);
        }

    }

    private String extractUserName(String email) {

        int indexOfAtSymbol = email.indexOf('@');
        return email.substring(0, indexOfAtSymbol);

    }

    private UserDTO buildUserDTO(User savedUser) {

        return UserDTO.
                builder()
                .id(savedUser.getId())
                .email(savedUser.getUsername())
                .username(extractUserName(savedUser.getUsername()))
                .build();

    }
}
