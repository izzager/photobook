package com.example.photobook.validator;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.exception.ResourceForbiddenException;
import com.example.photobook.repository.AlbumRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class UploadPhotoDtoValidator {

    private final AlbumRepository albumRepository;
    private final List<String> allowedContentTypes;

    public UploadPhotoDtoValidator(AlbumRepository albumRepository,
                                   @Value("#{'${photobookapp.allowed-photo-types}'.split(',')}") List<String> allowedContentTypes) {
        this.albumRepository = albumRepository;
        this.allowedContentTypes = allowedContentTypes;
    }

    public void checkPhotoUploadingFromComputer(UploadPhotoDto uploadPhotoDto, MultipartFile file) {
        if (!albumRepository.existsAlbumByIdAndUserOwnerUsername(uploadPhotoDto.getAlbumId(),
                                                                    uploadPhotoDto.getUsername())) {
            throw new ResourceForbiddenException("You are not the owner of this album");
        }
        if (uploadPhotoDto.getLink() != null) {
            throw new IllegalArgumentException("You upload photo from computer, not link");
        }
        checkIfContentTypeIsAllowed(file.getContentType());
    }

    public void checkPhotoUploadingByUrl(UploadPhotoDto uploadPhotoDto) {
        if (uploadPhotoDto.getLink() == null) {
            throw new IllegalArgumentException("Link must not be null");
        }
        checkUrlIsValid(uploadPhotoDto.getLink());
        checkIfContentTypeIsAllowed(FilenameUtils.getExtension(uploadPhotoDto.getLink()));
    }

    private void checkUrlIsValid(String link) {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(link)) {
            throw new IllegalArgumentException("Invalid link");
        }
    }

    private void checkIfContentTypeIsAllowed(String contentType) {
        if (!allowedContentTypes.contains(contentType)) {
            throw new IllegalArgumentException("This content type not allowed");
        }
    }

}
