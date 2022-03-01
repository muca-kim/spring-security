package com.project.security.repository;

import java.util.Optional;

import com.project.security.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    public Optional<User> findById(String userId);

}
