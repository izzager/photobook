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
import java.util.Optional;
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
        users.forEach(user -> findLastPostedPhoto(user)
                .ifPresentOrElse(photo ->
                                usersActivity.add(new UserActivityDto(user.getId(), user.getUsername(), photo.getLoadDate())),
                        () ->
                                usersActivity.add(new UserActivityDto(user.getId(), user.getUsername(), null))));
        return usersActivity;
    }

    @Override
    public List<PlatformUsageDto> getPlatformStatistics() {
        Map <String, Long> statistic = photoRepository
                .findAll().stream()
                .collect(Collectors.groupingBy(this::extractPhotoOrigin, Collectors.counting()));
        return toPlatformUsageDto(statistic);
    }

    private Optional<Photo> findLastPostedPhoto(User user) {
        List<Photo> lastPhotoInAlbum = new ArrayList<>();
        user.getAlbums().stream()
                .map(photoRepository::findTopByAlbumOrderByLoadDateDesc)
                .forEach(lastPhotoInAlbum::add);
        if (lastPhotoInAlbum.isEmpty()) {
            return Optional.empty();
        }
        Photo lastUserPhoto = Collections.max(lastPhotoInAlbum, Comparator.comparing(Photo::getLoadDate));
        return Optional.of(lastUserPhoto);
    }

    private List<PlatformUsageDto> toPlatformUsageDto(Map <String, Long> statistic) {
        List<PlatformUsageDto> platformUsage = new ArrayList<>();
        statistic.forEach((key, value) ->
                platformUsage.add(new PlatformUsageDto(key, value)));
        return platformUsage;
    }

    private String extractPhotoOrigin(Photo photo) {
        String loadSource = photo.getLoadSource();
        try {
            return new URL(loadSource).getHost();
        } catch (MalformedURLException e) {
            return loadSource;
        }
    }

}
