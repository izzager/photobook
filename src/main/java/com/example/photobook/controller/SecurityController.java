package com.example.photobook.controller;

import com.example.photobook.dto.UserDataDto;
import com.example.photobook.entity.User;
import com.example.photobook.mapperToEntity.UserDataDtoMapper;
import com.example.photobook.security.AuthResponse;
import com.example.photobook.security.JwtProvider;
import com.example.photobook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserDataDtoMapper userDataDtoMapper;

    @PostMapping("/registration")
    @ResponseStatus(value = HttpStatus.OK)
    public void registerUser(@RequestBody @Valid UserDataDto userDataDto) {
        userService.saveUser(userDataDtoMapper.toEntity(userDataDto));
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody UserDataDto userDataDto) {
        User user = userService.findByUsernameAndPassword(userDataDto.getUsername(), userDataDto.getPassword());
        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

}
