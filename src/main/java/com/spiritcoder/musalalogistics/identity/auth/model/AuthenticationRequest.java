package com.spiritcoder.musalalogistics.identity.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @Email(message = "email is not valid")
    @NotNull(message = "Email cannot be empty")
    protected String email;

    @NotNull(message = "password field cannot be empty")
    @Size(min = 8, message = "password must be at least 8 characters")
    protected String password;
}
