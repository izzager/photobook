package com.example.photobook.controller;

import com.example.photobook.exception.ControllerExceptionHandler;
import com.example.photobook.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void findLastUserActivities_passes() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders.get("/admin/findLastUserActivities"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getPlatformStatistics_passes() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders.get("/admin/getPlatformStatistics"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}