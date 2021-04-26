package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.helper.AlbumRepositoryHelper;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepositoryHelper albumRepositoryHelper;

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
