package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Photo;
import com.example.photobook.mapperToEntity.UploadPhotoDtoMapper;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.validator.UploadPhotoDtoValidator;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.example.photobook.TestConstants.ALLOWED_CONTENT_TYPES;
import static com.example.photobook.TestConstants.MILLISECONDS;
import static com.example.photobook.TestConstants.PATH_TO_FILES;
import static com.example.photobook.TestConstants.PHOTO_CONTENT_TYPE;
import static com.example.photobook.TestConstants.PHOTO_EXTENSION;
import static com.example.photobook.TestConstants.PHOTO_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoServiceImplTest {

    @InjectMocks
    private PhotoServiceImpl photoService;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UploadPhotoDtoMapper uploadPhotoDtoMapper;

    @Mock
    private UploadPhotoDtoValidator uploadPhotoDtoValidator;

    @Test
    public void uploadPhotoFromComputer_passes() throws IOException {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setPhotoName(PHOTO_NAME);
        Photo photo = new Photo();
        MultipartFile file = Mockito.mock(MultipartFile.class);
        InputStream inputStream  = IOUtils.toInputStream("test data", "UTF-8");

        when(file.getContentType()).thenReturn(PHOTO_CONTENT_TYPE);
        when(file.getOriginalFilename()).thenReturn(PHOTO_EXTENSION);
        ReflectionTestUtils.setField(photoService, "allowedContentTypes", ALLOWED_CONTENT_TYPES);
        ReflectionTestUtils.setField(photoService, "pathToFiles", PATH_TO_FILES);
        when(file.getInputStream()).thenReturn(inputStream);
        when(uploadPhotoDtoMapper.toEntity(uploadPhotoDto)).thenReturn(photo);
        when(photoRepository.save(photo)).thenReturn(photo);
        when(modelMapper.map(photo, PhotoDto.class)).thenReturn(new PhotoDto());

        PhotoDto result = photoService.uploadPhotoFromComputer(uploadPhotoDto, file);

        verify(uploadPhotoDtoValidator).checkPhotoUploadingFromComputer(uploadPhotoDto);
        verify(uploadPhotoDtoMapper).toEntity(uploadPhotoDto);
        verify(photoRepository).save(photo);
        verify(modelMapper).map(photo, PhotoDto.class);

        File tempFile = new File(PATH_TO_FILES + File.separator + uploadPhotoDto.getPhotoName());
        tempFile.delete();
    }

    @Test
    public void uploadPhotoFromComputer_notAllowedContentType_throwIllegalArgumentException() {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(file.getContentType()).thenReturn("ABC");
        ReflectionTestUtils.setField(photoService, "allowedContentTypes", ALLOWED_CONTENT_TYPES);

        assertThrows(IllegalArgumentException.class, () -> photoService.uploadPhotoFromComputer(uploadPhotoDto, file));
    }

    @Test
    public void uploadPhotoByUrl_passes() {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setPhotoName(PHOTO_NAME);
        Photo photo = new Photo();

        when(uploadPhotoDtoMapper.toEntity(uploadPhotoDto)).thenReturn(photo);
        when(photoRepository.save(photo)).thenReturn(photo);
        when(modelMapper.map(photo, PhotoDto.class)).thenReturn(new PhotoDto());

        PhotoDto result = photoService.uploadPhotoByUrl(uploadPhotoDto);

        verify(uploadPhotoDtoValidator).checkPhotoUploadingByUrl(uploadPhotoDto);
        verify(uploadPhotoDtoMapper).toEntity(uploadPhotoDto);
        verify(photoRepository).save(photo);
        verify(modelMapper).map(photo, PhotoDto.class);
    }

    @Test
    public void findLastPhotos_passes() {
        List<Photo> result = photoService.findLastPhotos(MILLISECONDS);

        verify(photoRepository).findAllByLoadDateAfter(Mockito.any());
    }

}