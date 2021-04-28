package com.example.photobook.util;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Photo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

public class PhotoUtils {

    private static final int suffixLength = 30;

    public static String buildPhotoName(UploadPhotoDto uploadPhotoDto, MultipartFile file) {
        return uploadPhotoDto.getPhotoName() + RandomStringUtils.randomAlphanumeric(suffixLength) +
                "." + FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public static String buildPhotoName(UploadPhotoDto uploadPhotoDto) {
        return uploadPhotoDto.getPhotoName() + RandomStringUtils.randomAlphanumeric(suffixLength) +
                "." + FilenameUtils.getExtension(uploadPhotoDto.getLink());
    }

    public static String buildPhotoName(Photo photo) {
        return photo.getPhotoName() + RandomStringUtils.randomAlphanumeric(suffixLength) +
                "." + FilenameUtils.getExtension(photo.getLoadSource());
    }

}
