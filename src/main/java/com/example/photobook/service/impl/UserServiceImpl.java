package com.example.photobook.service.impl;

import com.example.photobook.dto.UserDataDto;
import com.example.photobook.entity.User;
import com.example.photobook.mapperToEntity.UserDataDtoMapper;
import com.example.photobook.repository.RoleRepository;
import com.example.photobook.repository.UserRepository;
import com.example.photobook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDataDtoMapper userDataDtoMapper;

    private final String USER_ROLE = "ROLE_USER";

    @Override
    public User saveUser(UserDataDto userDataDto) {
        if (userRepository.existsUserByUsername(userDataDto.getUsername())) {
            throw new BadCredentialsException("User with this name already exists");
        }
        User user = userDataDtoMapper.toEntity(userDataDto);
        user.setRole(roleRepository.findRoleByRoleName(USER_ROLE));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = findByUsername(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new BadCredentialsException("Wrong password");
        }
    }
}
