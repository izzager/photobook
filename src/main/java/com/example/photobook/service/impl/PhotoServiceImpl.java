package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Photo;
import com.example.photobook.mapperToEntity.UploadPhotoDtoMapper;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.service.PhotoService;
import com.example.photobook.validator.UploadPhotoDtoValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final UploadPhotoDtoMapper uploadPhotoDtoMapper;
    private final UploadPhotoDtoValidator uploadPhotoDtoValidator;

    @Value("${photobookapp.downloading-directory}")
    private String pathToFiles;

    @Value("#{'${photobookapp.allowed-photo-types}'.split(',')}")
    private List<String> allowedContentTypes;

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
        checkIfContentTypeIsAllowed(file.getContentType());
        uploadPhotoDtoValidator.checkPhotoUploadingFromComputer(uploadPhotoDto);
        uploadPhotoDto.setPhotoName(buildPhotoName(uploadPhotoDto, file));
        savePhotoOnServer(uploadPhotoDto, file);
        Photo savedPhoto = photoRepository.save(uploadPhotoDtoMapper.toEntity(uploadPhotoDto));
        return modelMapper.map(savedPhoto, PhotoDto.class);
    }

    @Override
    public PhotoDto uploadPhotoByUrl(UploadPhotoDto uploadPhotoDto) {
        return null;
    }

    private void savePhotoOnServer(UploadPhotoDto uploadPhotoDto, MultipartFile file) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        File uploadedFile = new File(pathToFiles + File.separator + uploadPhotoDto.getPhotoName());
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    private void checkIfContentTypeIsAllowed(String contentType) {
        if (!allowedContentTypes.contains(contentType)) {
            throw new IllegalArgumentException("This content type not allowed");
        }
    }

    private String generatingRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 30;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String buildPhotoName(UploadPhotoDto uploadPhotoDto, MultipartFile file) {
        return uploadPhotoDto.getPhotoName() + generatingRandomString() +
                "." + FilenameUtils.getExtension(file.getOriginalFilename());
    }

}
