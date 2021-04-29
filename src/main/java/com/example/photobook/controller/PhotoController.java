package com.example.photobook.controller;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("albums/{albumId}/photos")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("uploadFromComputer")
    public PhotoDto uploadFromComputer(@PathVariable Long albumId,
                                       @Validated @RequestPart(name = "photoData") UploadPhotoDto uploadPhotoDto,
                                       @RequestPart(name = "photo") MultipartFile file) throws IOException {
        uploadPhotoDto.setAlbumId(albumId);
        return photoService.uploadPhotoFromComputer(uploadPhotoDto, file);
    }

    @PostMapping("uploadByUrl")
    public PhotoDto uploadByUrl(@PathVariable Long albumId,
                                @Validated @RequestBody UploadPhotoDto uploadPhotoDto) {
        uploadPhotoDto.setAlbumId(albumId);
        return photoService.uploadPhotoByUrl(uploadPhotoDto);
    }

    @GetMapping
    public List<PhotoDto> findAllPhotosInAlbum(@PathVariable Long albumId) {
        return photoService.findAllPhotosInAlbum(albumId);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
    }

}
