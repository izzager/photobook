package com.example.photobook.controller;

import com.example.photobook.dto.UserDataDto;
import com.example.photobook.entity.User;
import com.example.photobook.exception.ControllerExceptionHandler;
import com.example.photobook.security.JwtProvider;
import com.example.photobook.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.photobook.TestConstants.TOKEN;
import static com.example.photobook.TestConstants.USERNAME;
import static com.example.photobook.TestConstants.USER_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private SecurityController securityController;

    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(securityController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void registerUser_correctDto_OK() throws Exception {
        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setUsername(USERNAME);
        userDataDto.setPassword(USER_PASSWORD);

        MockHttpServletResponse response =
                mvc.perform(MockMvcRequestBuilders
                        .post("/registration")
                        .content(new ObjectMapper().writeValueAsString(userDataDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        verify(userService).saveUser(userDataDto);
    }

    @Test
    public void registerUser_emptyFields_BadRequest() throws Exception {
        UserDataDto userDataDto = new UserDataDto();

        MockHttpServletResponse response =
                mvc.perform(MockMvcRequestBuilders
                        .post("/registration")
                        .content(new ObjectMapper().writeValueAsString(userDataDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void auth_correctDto_OK() throws Exception {
        UserDataDto userDataDto = new UserDataDto();
        userDataDto.setUsername(USERNAME);
        userDataDto.setPassword(USER_PASSWORD);
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(USER_PASSWORD);

        when(userService.findByUsernameAndPassword(USERNAME, USER_PASSWORD)).thenReturn(user);
        when(jwtProvider.generateToken(USERNAME)).thenReturn(TOKEN);
        MockHttpServletResponse response =
                mvc.perform(MockMvcRequestBuilders
                        .post("/auth")
                        .content(new ObjectMapper().writeValueAsString(userDataDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        verify(userService).findByUsernameAndPassword(USERNAME, USER_PASSWORD);
        verify(jwtProvider).generateToken(USERNAME);
    }

    @Test
    public void auth_emptyFields_BadRequest() throws Exception {
        UserDataDto userDataDto = new UserDataDto();

        MockHttpServletResponse response =
                mvc.perform(MockMvcRequestBuilders
                        .post("/auth")
                        .content(new ObjectMapper().writeValueAsString(userDataDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

}