package com.example.photobook.controller;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("albums/{albumId}/photos")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("uploadFromComputer")
    public PhotoDto uploadFromComputer(@PathVariable Long albumId,
                                       @RequestPart(name = "photoData") UploadPhotoDto uploadPhotoDto,
                                       @RequestPart(name = "photo") MultipartFile file) throws IOException {
        uploadPhotoDto.setAlbumId(albumId);
        return photoService.uploadPhotoFromComputer(uploadPhotoDto, file);
    }

    @PostMapping("uploadByUrl")
    public void uploadByUrl(@PathVariable Long albumId,
                            @RequestBody UploadPhotoDto uploadPhotoDto) {
        uploadPhotoDto.setAlbumId(albumId);
        photoService.uploadPhotoByUrl(uploadPhotoDto);
    }

}
