package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.service.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {
    @Override
    public List<PhotoDto> findAllPhotosInAlbum(Long albumId) {
        return null;
    }

    @Override
    public PhotoDto deletePhoto(Long photoId) {
        return null;
    }

    @Override
    public void findPhotoById(Long photoId) {

    }

    @Override
    public PhotoDto uploadPhotoFromComputer(PhotoDto photoDto, MultipartFile file) {
        return null;
    }

    @Override
    public PhotoDto uploadPhotoByUrl(PhotoDto photoDto, String link) {
        return null;
    }
}
