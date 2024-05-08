package com.spiritcoder.musalalogistics.identity.users.repository;

import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.identity.users.entity.User;
import com.spiritcoder.musalalogistics.identity.users.enums.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserEntityManager {

    private static final Logger LOG = LoggerFactory.getLogger(UserEntityManager.class);

    private final UserRepository userRepository;

    public Optional<User> saveUser(String email, String username, String password, Role role, boolean enabled, LocalDateTime created, String creator, String updater){
        try{
            userRepository.saveUser(email, username, password, role.toString(), enabled, created, creator, updater);
            return userRepository.findByEmail(email);
        }catch(MusalaLogisticsException musalaLogisticsException){
            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
        return Optional.empty();
    }

    public Optional<User> getUserByEmail(String email){
       return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }
}
