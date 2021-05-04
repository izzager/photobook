package com.example.photobook.service;

import com.example.photobook.entity.User;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
