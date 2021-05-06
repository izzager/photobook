package com.example.photobook.service.impl;

import com.example.photobook.dto.PlatformUsageDto;
import com.example.photobook.dto.UserActivityDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.Photo;
import com.example.photobook.entity.User;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.photobook.TestConstants.PHOTO_COMPUTER_LOAD;
import static com.example.photobook.TestConstants.PHOTO_HOST;
import static com.example.photobook.TestConstants.PHOTO_LINK;
import static com.example.photobook.TestConstants.USERNAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void getPlatformStatistics_passes() {
        List<Photo> photos = new ArrayList<>();
        Photo photo1 = new Photo();
        photo1.setLoadSource(PHOTO_LINK);
        photos.add(photo1);
        Photo photo2 = new Photo();
        photo2.setLoadSource(PHOTO_COMPUTER_LOAD);
        photos.add(photo2);

        when(photoRepository.findAll()).thenReturn(photos);
        List<PlatformUsageDto> result = adminService.getPlatformStatistics();

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(PHOTO_HOST, result.get(0).getPlatformName()),
                () -> assertEquals(1, result.get(0).getNumberOfUses()),
                () -> assertEquals(PHOTO_COMPUTER_LOAD, result.get(1).getPlatformName()),
                () -> assertEquals(1, result.get(1).getNumberOfUses())
        );
        verify(photoRepository).findAll();
    }

    @Test
    public void findLastUserActivities() {
        User activeUser = new User();
        activeUser.setId(1L);
        activeUser.setUsername(USERNAME);
        User inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setUsername(USERNAME);

        List<User> users = new ArrayList<>();
        users.add(activeUser);
        users.add(inactiveUser);

        Photo photo = new Photo();
        photo.setLoadDate(LocalDateTime.MAX);

        Album album = new Album();
        album.setUserOwner(activeUser);
        album.setPhotos(new ArrayList<>());
        album.getPhotos().add(photo);
        activeUser.setAlbums(new ArrayList<>());
        activeUser.getAlbums().add(album);

        when(userRepository.findAll()).thenReturn(users);
        when(photoRepository.findTopByAlbumOrderByLoadDateDesc(album)).thenReturn(photo);
        List<UserActivityDto> result = adminService.findLastUserActivities();

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(activeUser.getId(), result.get(0).getUserId()),
                () -> assertEquals(LocalDateTime.MAX, result.get(0).getLastActivityDate()),
                () -> assertEquals(inactiveUser.getId(), result.get(1).getUserId()),
                () -> assertNull(result.get(1).getLastActivityDate())
        );
        verify(userRepository).findAll();
        verify(photoRepository).findTopByAlbumOrderByLoadDateDesc(album);
    }

}