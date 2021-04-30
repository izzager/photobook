package com.example.photobook.controller;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.exception.ControllerExceptionHandler;
import com.example.photobook.service.AlbumService;
import com.example.photobook.service.PhotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;

import static com.example.photobook.TestConstants.ALBUM_ID;
import static com.example.photobook.TestConstants.PHOTO_ID;
import static com.example.photobook.TestConstants.PHOTO_LINK;
import static com.example.photobook.TestConstants.PHOTO_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PhotoControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private PhotoController photoController;

    @Mock
    private PhotoService photoService;

    @Mock
    private AlbumService albumService;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(photoController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void uploadFromComputer_passes() throws Exception {
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

    @Test
    public void uploadByUrl_passes() throws Exception {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setPhotoName(PHOTO_NAME);
        uploadPhotoDto.setLink(PHOTO_LINK);

        mvc.perform(MockMvcRequestBuilders
                        .post("/albums/{albumId}/photos/uploadByUrl", ALBUM_ID)
                        .content(new ObjectMapper().writeValueAsString(uploadPhotoDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    public void findAllPhotosInAlbum_passes() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders.get("/albums/{albumId}/photos", ALBUM_ID))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void deletePhoto_passes() throws Exception {
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders
                        .delete("/albums/{albumId}/photos/{id}", ALBUM_ID, PHOTO_ID))
                .andReturn().getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void downloadPhoto_passes() throws Exception {
        File file = Mockito.mock(File.class);
        when(photoService.findPhotoById(PHOTO_ID, ALBUM_ID)).thenReturn(file);
        when(file.getName()).thenReturn(PHOTO_NAME);
        byte[] bytes = new byte[1];
        try (MockedStatic<FileUtils> fileUtilsMockedStatic = Mockito.mockStatic(FileUtils.class)) {
            fileUtilsMockedStatic.when(() -> FileUtils.readFileToByteArray(file))
                    .thenReturn(bytes);
        }
        MockHttpServletResponse response = mvc
                .perform(MockMvcRequestBuilders
                        .get("/albums/{albumId}/photos/{id}", ALBUM_ID, PHOTO_ID))
                .andReturn().getResponse();
        assertEquals(response.getHeader("Content-Disposition"),
                "attachment; filename=" + PHOTO_NAME);
    }

}