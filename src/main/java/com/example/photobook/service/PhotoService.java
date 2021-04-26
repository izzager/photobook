package com.example.photobook.service;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    List<PhotoDto> findAllPhotosInAlbum(Long albumId);
    PhotoDto deletePhoto(Long photoId);
    void findPhotoById(Long photoId);
    PhotoDto uploadPhotoFromComputer(UploadPhotoDto uploadPhotoDto, MultipartFile file) throws IOException;
    PhotoDto uploadPhotoByUrl(UploadPhotoDto uploadPhotoDto);
}
