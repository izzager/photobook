package com.example.photobook.mapperToEntity;

import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.Photo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadPhotoDtoMapper implements Mapper<UploadPhotoDto, Photo> {

    private final ModelMapper modelMapper;

    @Value("${photobookapp.home-uploading-name}")
    private String fromComputer;

    @Override
    public Photo toEntity(UploadPhotoDto uploadPhotoDto) {
        Photo photo = new Photo();
        Album album = new Album();
        album.setId(uploadPhotoDto.getAlbumId());
        photo.setAlbum(album);
        photo.setPhotoName(uploadPhotoDto.getPhotoName());
        photo.setLoadSource(uploadPhotoDto.getLink());
        if (photo.getLoadSource() == null) {
            photo.setLoadSource(fromComputer);
        }
        return photo;
    }
}
