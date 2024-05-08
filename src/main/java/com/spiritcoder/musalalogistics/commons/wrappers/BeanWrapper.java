package com.spiritcoder.musalalogistics.commons.wrappers;

import com.spiritcoder.musalalogistics.identity.auth.model.AuthenticationRequest;
import com.spiritcoder.musalalogistics.identity.users.enums.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Configuration
public class BeanWrapper {

    public static UsernamePasswordAuthenticationToken getAuthenticationTokenBean(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()
        );
    }

    public static UsernamePasswordAuthenticationToken getAuthenticationBean(
            AuthenticationRequest authenticationRequest) {
        return new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                                    authenticationRequest.getPassword());
    }

    public static WebAuthenticationDetailsSource getAuthenticationDetailsSource() {
        return new WebAuthenticationDetailsSource();
    }

    public static SimpleGrantedAuthority getGrantedAuthorityBean(Role role) {
        return new SimpleGrantedAuthority(role.name());
    }

    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
