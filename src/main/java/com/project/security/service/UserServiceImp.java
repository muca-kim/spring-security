package com.project.security.service;

import java.util.Objects;

import com.project.security.model.User;
import com.project.security.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("not found user id"));
    }

    @Override
    public void addUser(User user) throws Exception {
        String userId = Objects.requireNonNull(user.getUserId(), "user");
        if (userRepository.findById(userId).isPresent()) {
            throw new Exception("ID already registered");
        }
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void findAllUser() {
        // TODO Auto-generated method stub

    }

}