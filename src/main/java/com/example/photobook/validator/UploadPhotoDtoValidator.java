package com.example.photobook.validator;

import com.example.photobook.dto.UploadPhotoDto;
import org.springframework.stereotype.Component;

@Component
public class UploadPhotoDtoValidator {

    public void checkPhotoUploadingFromComputer(UploadPhotoDto uploadPhotoDto) {
        if (uploadPhotoDto.getLink() != null) {
            throw new IllegalArgumentException("You upload photo from computer, not link");
        }
    }

}
