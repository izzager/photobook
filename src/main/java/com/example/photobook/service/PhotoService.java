package com.example.photobook.service;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface PhotoService {
    void deletePhoto(Long photoId);
    File findPhotoById(Long photoId, Long albumId) throws IOException;
    PhotoDto uploadPhotoFromComputer(UploadPhotoDto uploadPhotoDto, MultipartFile file) throws IOException;
    PhotoDto uploadPhotoByUrl(UploadPhotoDto uploadPhotoDto);
}
