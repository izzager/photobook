package com.example.photobook.service.impl;

import com.example.photobook.dto.UserDataDto;
import com.example.photobook.entity.Role;
import com.example.photobook.entity.User;
import com.example.photobook.mapperToEntity.UserDataDtoMapper;
import com.example.photobook.repository.RoleRepository;
import com.example.photobook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.photobook.TestConstants.USERNAME;
import static com.example.photobook.TestConstants.USER_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDataDtoMapper userDataDtoMapper;

    @Test
    public void saveUser_newUsername_passes() {
        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setUsername(USERNAME);
        User user = new User();
        user.setUsername(USERNAME);

        when(userRepository.existsUserByUsername(userDataDto.getUsername())).thenReturn(false);
        when(userDataDtoMapper.toEntity(userDataDto)).thenReturn(user);
        when(roleRepository.findRoleByRoleName(USER_ROLE)).thenReturn(new Role());
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.saveUser(userDataDto);

        assertEquals(USERNAME, result.getUsername());
        verify(userRepository).existsUserByUsername(USERNAME);
        verify(userDataDtoMapper).toEntity(userDataDto);
        verify(roleRepository).findRoleByRoleName(USER_ROLE);
        verify(userRepository).save(user);
    }

    @Test
    public void saveUser_existedUsername_throwsBadCredentialsException() {
        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setUsername(USERNAME);

        when(userRepository.existsUserByUsername(userDataDto.getUsername())).thenReturn(true);

        assertThrows(BadCredentialsException.class, () -> userService.saveUser(userDataDto));
    }

    @Test
    public void findByUsername_userExists_passes() {
        User user = new User();
        user.setUsername(USERNAME);

        when(userRepository.findUserByUsername(USERNAME)).thenReturn(Optional.of(user));
        User result = userService.findByUsername(USERNAME);

        assertEquals(USERNAME, result.getUsername());
        verify(userRepository).findUserByUsername(USERNAME);
    }

    @Test
    public void findByUsername_userNotExists_throwUsernameNotFoundException() {
        User user = new User();
        user.setUsername(USERNAME);

        when(userRepository.findUserByUsername(USERNAME)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(USERNAME));

        verify(userRepository).findUserByUsername(USERNAME);
    }

}