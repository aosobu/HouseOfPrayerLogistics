package com.spiritcoder.musalalogistics.identity.users.entity;

import com.spiritcoder.musalalogistics.commons.entity.AbstractAuditable;
import com.spiritcoder.musalalogistics.commons.wrappers.BeanWrapper;
import com.spiritcoder.musalalogistics.identity.users.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractAuditable implements UserDetails {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    private String email;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    public User(LocalDateTime created, LocalDateTime updated, String creator, String updater,
                String email, String username, String password, Role role, boolean enabled) {
        super(created, updated, creator, updater);
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(BeanWrapper.getGrantedAuthorityBean(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
