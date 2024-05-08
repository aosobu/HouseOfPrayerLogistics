package com.spiritcoder.musalalogistics.identity.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private Integer id;

    private String username;

    private String email;
}
