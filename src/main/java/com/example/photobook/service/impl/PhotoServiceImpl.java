package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Photo;
import com.example.photobook.mapperToEntity.UploadPhotoDtoMapper;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.service.PhotoService;
import com.example.photobook.validator.UploadPhotoDtoValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.photobook.util.PhotoUtils.buildPhotoName;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final UploadPhotoDtoMapper uploadPhotoDtoMapper;
    private final UploadPhotoDtoValidator uploadPhotoDtoValidator;

    @Value("${photobookapp.downloading-directory}")
    private String pathToFiles;

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
    public PhotoDto uploadPhotoFromComputer(UploadPhotoDto uploadPhotoDto,
                                            MultipartFile file) throws IOException {
        uploadPhotoDtoValidator.checkPhotoUploadingFromComputer(uploadPhotoDto, file);
        uploadPhotoDto.setPhotoName(buildPhotoName(uploadPhotoDto, file));
        savePhotoOnServer(uploadPhotoDto, file);
        Photo savedPhoto = photoRepository.save(uploadPhotoDtoMapper.toEntity(uploadPhotoDto));
        return modelMapper.map(savedPhoto, PhotoDto.class);
    }

    @Override
    public PhotoDto uploadPhotoByUrl(UploadPhotoDto uploadPhotoDto) {
        uploadPhotoDtoValidator.checkPhotoUploadingByUrl(uploadPhotoDto);
        uploadPhotoDto.setPhotoName(buildPhotoName(uploadPhotoDto));
        Photo savedPhoto = photoRepository.save(uploadPhotoDtoMapper.toEntity(uploadPhotoDto));
        return modelMapper.map(savedPhoto, PhotoDto.class);
    }

    @Override
    public List<Photo> findLastPhotos(Long millis) {
        LocalDateTime time = LocalDateTime.now().minusSeconds(millis * 2 / 1000);
        return photoRepository.findAllByLoadDateAfter(time);
    }

    private void savePhotoOnServer(UploadPhotoDto uploadPhotoDto, MultipartFile file) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        File uploadedFile = new File(pathToFiles + File.separator + uploadPhotoDto.getPhotoName());
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
        stream.write(bytes);
        stream.flush();
        stream.close();
    }

}
