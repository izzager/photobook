package com.example.photobook.controller;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.exception.ControllerExceptionHandler;
import com.example.photobook.service.PhotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.photobook.TestConstants.ALBUM_ID;
import static com.example.photobook.TestConstants.PHOTO_NAME;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PhotoControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private PhotoController photoController;

    @Mock
    private PhotoService photoService;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(photoController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void uploadFromComputer_passes() throws Exception {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setPhotoName(PHOTO_NAME);
        MockMultipartFile firstFile = new MockMultipartFile("photo", "filename.jpeg",
                "image/jpeg", "some file".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("photoData", "",
                "application/json", "{\"photoName\": \"someValue\"}".getBytes());

        mvc.perform(MockMvcRequestBuilders
                        .multipart("/albums/{albumId}/photos/uploadFromComputer", ALBUM_ID)
                        .file(firstFile)
                        .file(jsonFile))
                .andExpect(status().is(200));
    }

}