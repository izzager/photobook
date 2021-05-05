package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.Photo;
import com.example.photobook.entity.User;
import com.example.photobook.helper.PhotoRepositoryHelper;
import com.example.photobook.mapperToEntity.UploadPhotoDtoMapper;
import com.example.photobook.repository.AlbumRepository;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.security.UserContext;
import com.example.photobook.util.DownloadingStatusHelper;
import com.example.photobook.util.LoadingPhotoByURLHelper;
import com.example.photobook.validator.UploadPhotoDtoValidator;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Optional;

import static com.example.photobook.TestConstants.ALBUM_ID;
import static com.example.photobook.TestConstants.PATH_TO_FILES;
import static com.example.photobook.TestConstants.PHOTO_ID;
import static com.example.photobook.TestConstants.PHOTO_LINK;
import static com.example.photobook.TestConstants.PHOTO_NAME;
import static com.example.photobook.TestConstants.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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

    @Mock
    private PhotoRepositoryHelper photoRepositoryHelper;

    @Mock
    private DownloadingStatusHelper downloadingStatusHelper;

    @Mock
    private LoadingPhotoByURLHelper loadingPhotoByURLHelper;

    @Mock
    private UserContext userContext;

    @Mock
    private AlbumRepository albumRepository;

    @Test
    public void uploadPhotoFromComputer_passes() throws IOException {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setPhotoName(PHOTO_NAME);
        uploadPhotoDto.setAlbumId(ALBUM_ID);
        Photo photo = new Photo();
        MultipartFile file = Mockito.mock(MultipartFile.class);
        InputStream inputStream  = IOUtils.toInputStream("test data", "UTF-8");
        Authentication authentication = Mockito.mock(Authentication.class);

        when(userContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(albumRepository.existsAlbumByIdAndUserOwnerUsername(ALBUM_ID,
                userContext.getAuthentication().getName())).thenReturn(true);
        ReflectionTestUtils.setField(photoService, "pathToFiles", PATH_TO_FILES);
        when(file.getInputStream()).thenReturn(inputStream);
        when(uploadPhotoDtoMapper.toEntity(uploadPhotoDto)).thenReturn(photo);
        when(photoRepository.save(photo)).thenReturn(photo);
        when(modelMapper.map(photo, PhotoDto.class)).thenReturn(new PhotoDto());

        PhotoDto result = photoService.uploadPhotoFromComputer(uploadPhotoDto, file);

        verify(uploadPhotoDtoValidator).checkPhotoUploadingFromComputer(uploadPhotoDto, file);
        verify(uploadPhotoDtoMapper).toEntity(uploadPhotoDto);
        verify(photoRepository).save(photo);
        verify(modelMapper).map(photo, PhotoDto.class);

        File tempFile = new File(PATH_TO_FILES + File.separator + uploadPhotoDto.getPhotoName());
        tempFile.delete();
    }

    @Test
    public void uploadPhotoFromComputer_notAllowedContentType_throwIllegalArgumentException() {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setAlbumId(ALBUM_ID);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        when(userContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(albumRepository.existsAlbumByIdAndUserOwnerUsername(ALBUM_ID,
                userContext.getAuthentication().getName())).thenReturn(true);
        doThrow(IllegalArgumentException.class)
                .when(uploadPhotoDtoValidator).checkPhotoUploadingFromComputer(uploadPhotoDto, file);

        assertThrows(IllegalArgumentException.class, () -> photoService.uploadPhotoFromComputer(uploadPhotoDto, file));
        verify(uploadPhotoDtoValidator).checkPhotoUploadingFromComputer(uploadPhotoDto, file);
    }

    @Test
    public void uploadPhotoByUrl_passes() {
        UploadPhotoDto uploadPhotoDto = new UploadPhotoDto();
        uploadPhotoDto.setPhotoName(PHOTO_NAME);
        uploadPhotoDto.setAlbumId(ALBUM_ID);
        Photo photo = new Photo();
        Authentication authentication = Mockito.mock(Authentication.class);

        when(userContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(albumRepository.existsAlbumByIdAndUserOwnerUsername(ALBUM_ID,
                userContext.getAuthentication().getName())).thenReturn(true);
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
    public void deletePhoto_passes() {
        Photo photo = new Photo();
        photo.setAlbum(new Album());
        photo.getAlbum().setUserOwner(new User());
        photo.getAlbum().getUserOwner().setUsername(USERNAME);
        Authentication authentication = Mockito.mock(Authentication.class);

        when(userContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(photoRepository.findById(PHOTO_ID)).thenReturn(Optional.of(photo));
        photoService.deletePhoto(PHOTO_ID);

        verify(photoRepository).findById(PHOTO_ID);
        verify(photoRepository).deleteById(PHOTO_ID);
    }

    @Test
    public void findPhotoById_photoIsOnServer_passes() throws IOException {
        Photo photo = new Photo();
        photo.setId(PHOTO_ID);
        photo.setAlbum(new Album());
        photo.getAlbum().setId(ALBUM_ID);
        photo.setPhotoName(PHOTO_NAME);
        File file = Mockito.mock(File.class);

        when(photoRepositoryHelper.ensurePhotoExists(ALBUM_ID, PHOTO_ID)).thenReturn(photo);
        when(downloadingStatusHelper.findLocalFileInstance(PHOTO_NAME))
                .thenReturn(Optional.ofNullable(file));
        File result = photoService.findPhotoById(ALBUM_ID, PHOTO_ID);

        assertEquals(file, result);
        verify(photoRepositoryHelper).ensurePhotoExists(ALBUM_ID, PHOTO_ID);
        verify(downloadingStatusHelper).findLocalFileInstance(PHOTO_NAME);
    }

    @Test
    public void findPhotoById_photoIsNotOnServer_passes() throws IOException {
        Photo photo = new Photo();
        photo.setId(PHOTO_ID);
        photo.setAlbum(new Album());
        photo.getAlbum().setId(ALBUM_ID);
        photo.setPhotoName(PHOTO_NAME);
        photo.setLoadSource(PHOTO_LINK);

        ReflectionTestUtils.setField(photoService, "pathToFiles", PATH_TO_FILES);
        when(photoRepositoryHelper.ensurePhotoExists(ALBUM_ID, PHOTO_ID)).thenReturn(photo);
        when(downloadingStatusHelper.findLocalFileInstance(PHOTO_NAME))
                .thenReturn(Optional.empty());
        File result = photoService.findPhotoById(ALBUM_ID, PHOTO_ID);

        verify(photoRepositoryHelper).ensurePhotoExists(ALBUM_ID, PHOTO_ID);
        verify(downloadingStatusHelper).findLocalFileInstance(PHOTO_NAME);
        verify(loadingPhotoByURLHelper).downloadPhotoFromUrl(PHOTO_LINK, Paths.get(PATH_TO_FILES, PHOTO_NAME));
    }

}