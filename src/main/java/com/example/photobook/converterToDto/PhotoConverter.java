package com.example.photobook.converterToDto;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.entity.Photo;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PhotoConverter implements Converter<Photo, PhotoDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public PhotoDto convert(MappingContext<Photo, PhotoDto> mappingContext) {
        Photo photo = mappingContext.getSource();
        PhotoDto photoDto = mappingContext.getDestination();
        if (photoDto == null) {
            photoDto = new PhotoDto();
        }
        modelMapper.map(photo, photoDto);
        return photoDto;
    }
}
