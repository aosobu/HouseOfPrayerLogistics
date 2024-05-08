package com.spiritcoder.musalalogistics.identity.users.repository;

import com.spiritcoder.musalalogistics.identity.users.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "insert into users(email, username, password, role, enabled, created, creator, updater) values(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery = true)
    void saveUser(String email, String username, String password, String role,
                  boolean enabled, LocalDateTime created, String creator, String updater);
}
