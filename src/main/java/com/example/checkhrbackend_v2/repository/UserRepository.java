package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Role;
import com.example.checkhrbackend_v2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByRole(Role role);
    Optional<User> findUsersByUsername(String email);

    User findByUsername(String username);

    User findByPasswordResetToken(String passwordResetToken);
}