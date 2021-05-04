package com.example.photobook.service;

import com.example.photobook.dto.UserDataDto;
import com.example.photobook.entity.User;

public interface UserService {
    User saveUser(UserDataDto userDataDto);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
