package com.spiritcoder.musalalogistics.identity.users.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spiritcoder.musalalogistics.identity.users.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private UserDTO userDTO;

    @JsonProperty("errors")
    private List<String> errors = new ArrayList<>();

    public boolean isEmptyErrorList(){
        return this.getErrors().isEmpty();
    }
}
