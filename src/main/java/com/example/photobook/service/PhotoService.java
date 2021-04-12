package com.example.photobook.service;

import com.example.photobook.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    List<PhotoDto> findAllPhotosInAlbum(Long albumId);
    PhotoDto deletePhoto(Long photoId);
    void findPhotoById(Long photoId);
    PhotoDto uploadPhotoFromComputer(PhotoDto photoDto, MultipartFile file);
    PhotoDto uploadPhotoByUrl(PhotoDto photoDto, String link);
}
