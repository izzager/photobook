package com.example.photobook.controller;

import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.exception.ControllerExceptionHandler;
import com.example.photobook.service.AlbumService;
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

import static com.example.photobook.TestConstants.ALBUM_ID;
import static com.example.photobook.TestConstants.ALBUM_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AlbumControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private AlbumController albumController;

    @Mock
    private AlbumService albumService;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(albumController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void findAllAlbums_passes() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders.get("/albums"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void deleteAlbum_passes() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders.delete("/albums/{id}", ALBUM_ID))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void createAlbum_passes() throws Exception {
        CreateAlbumDto albumDto = new CreateAlbumDto();
        albumDto.setAlbumName(ALBUM_NAME);

        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders
                        .post("/albums")
                        .content(new ObjectMapper().writeValueAsString(albumDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void createAlbum_blankAlbumName_throwsHttpCode400() throws Exception {
        CreateAlbumDto albumDto = new CreateAlbumDto();

        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders
                        .post("/albums")
                        .content(new ObjectMapper().writeValueAsString(albumDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

}