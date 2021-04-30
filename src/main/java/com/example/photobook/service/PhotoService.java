package com.example.photobook.service;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface PhotoService {
    void deletePhoto(Long photoId);
    File findPhotoById(Long albumId, Long photoId) throws IOException;
    PhotoDto uploadPhotoFromComputer(UploadPhotoDto uploadPhotoDto, MultipartFile file) throws IOException;
    PhotoDto uploadPhotoByUrl(UploadPhotoDto uploadPhotoDto);
}
