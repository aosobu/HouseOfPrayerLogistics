package com.spiritcoder.musalalogistics.identity.users.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @Email(message = "email is not valid")
    @NotNull(message = "email cannot be empty")
    protected String email;

    @NotNull(message = "password cannot be empty")
    @Size(min = 8, message = "password must be at least 8 characters")
    protected String password;

}
