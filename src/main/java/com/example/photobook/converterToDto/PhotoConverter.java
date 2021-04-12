package com.example.photobook.converterToDto;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.entity.Photo;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PhotoConverter implements Converter<Photo, PhotoDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${photobookapp.downloading-directory}")
    private String hostPath;

    @Override
    public PhotoDto convert(MappingContext<Photo, PhotoDto> mappingContext) {
        Photo photo = mappingContext.getSource();
        PhotoDto photoDto = mappingContext.getDestination();
        if (photoDto == null) {
            photoDto = new PhotoDto();
        }
        modelMapper.map(photo, photoDto);
        photoDto.setAlbumId(photo.getAlbum().getId());
        photoDto.setLink(hostPath + photo.getPhotoName());
        return photoDto;
    }
}