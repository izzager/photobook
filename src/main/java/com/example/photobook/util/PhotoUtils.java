package com.example.photobook.util;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Photo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Component
public class PhotoUtils {

    private static String generatingRandomString() {
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

    public static String buildPhotoName(UploadPhotoDto uploadPhotoDto, MultipartFile file) {
        return uploadPhotoDto.getPhotoName() + generatingRandomString() +
                "." + FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public static String buildPhotoName(UploadPhotoDto uploadPhotoDto) {
        return uploadPhotoDto.getPhotoName() + generatingRandomString() +
                "." + FilenameUtils.getExtension(uploadPhotoDto.getLink());
    }

    public static String buildPhotoName(Photo photo) {
        return photo.getPhotoName() + generatingRandomString() +
                "." + FilenameUtils.getExtension(photo.getLoadSource());
    }

}
