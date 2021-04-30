package com.example.photobook.helper;

import com.example.photobook.entity.Photo;
import com.example.photobook.exception.ResourceNotFoundException;
import com.example.photobook.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhotoRepositoryHelper {

    private final PhotoRepository photoRepository;

    public Photo ensurePhotoExists(Long photoId) {
        return photoRepository
                .findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
    }

    public Photo ensurePhotoExists(Long albumId, Long photoId) {
        return photoRepository
                .findById(photoId)
                .filter(photo -> photo.getAlbum().getId().equals(albumId))
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
    }

}
