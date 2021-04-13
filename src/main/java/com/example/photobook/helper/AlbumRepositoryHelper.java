package com.example.photobook.helper;

import com.example.photobook.entity.Album;
import com.example.photobook.exception.ResourceNotFoundException;
import com.example.photobook.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumRepositoryHelper {

    private final AlbumRepository albumRepository;

    public Album ensureAlbumExists(Long albumId) {
        return albumRepository
                .findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));
    }
}
