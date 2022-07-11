package com.project.security.service;

import com.project.security.model.User;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {

    public void addUser(User user) throws Exception;

    public void delete(User user);

    public void findAllUser();

}
