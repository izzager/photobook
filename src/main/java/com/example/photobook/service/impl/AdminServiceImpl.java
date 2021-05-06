package com.example.photobook.service.impl;

import com.example.photobook.dto.PlatformUsageDto;
import com.example.photobook.dto.UserActivityDto;
import com.example.photobook.entity.Photo;
import com.example.photobook.entity.User;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.repository.UserRepository;
import com.example.photobook.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserActivityDto> findLastUserActivities() {
        List<User> users = userRepository.findAll();
        List<UserActivityDto> usersActivity = new ArrayList<>();
        for (User user : users) {
            List<Photo> lastPhotoInAlbum = findLastPhotoInEachAlbum(user);
            if (lastPhotoInAlbum.isEmpty()) {
                usersActivity.add(new UserActivityDto(user.getId(), user.getUsername(), null));
                continue;
            }
            usersActivity.add(getLastUserActivity(user, lastPhotoInAlbum));
        }
        return usersActivity;
    }

    @Override
    public List<PlatformUsageDto> getPlatformStatistics() {
        Map <String, Long> statistic = photoRepository
                .findAll().stream()
                .collect(Collectors.groupingBy(photo -> {
                            String loadSource = photo.getLoadSource();
                            try {
                                return new URL(photo.getLoadSource()).getHost();
                            } catch (MalformedURLException e) {
                                return loadSource;
                            }
                        },
                        Collectors.counting()));
        return toPlatformUsageDto(statistic);
    }

    private List<Photo> findLastPhotoInEachAlbum(User user) {
        List<Photo> lastPhotoInAlbum = new ArrayList<>();
        user.getAlbums().stream()
                .map(photoRepository::findTopByAlbumOrderByLoadDateDesc)
                .forEach(lastPhotoInAlbum::add);
        return lastPhotoInAlbum;
    }

    private UserActivityDto getLastUserActivity(User user, List<Photo> lastPhotoInAlbum) {
        Photo lastUserPhoto = Collections.max(lastPhotoInAlbum, Comparator.comparing(Photo::getLoadDate));
        return new UserActivityDto(user.getId(), user.getUsername(), lastUserPhoto.getLoadDate());
    }

    private List<PlatformUsageDto> toPlatformUsageDto(Map <String, Long> statistic) {
        List<PlatformUsageDto> platformUsage = new ArrayList<>();
        statistic.forEach((key, value) ->
                platformUsage.add(new PlatformUsageDto(key, value)));
        return platformUsage;
    }
}
