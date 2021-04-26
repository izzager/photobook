package com.example.photobook.configuration;

import com.example.photobook.converterToDto.AlbumDtoConverter;
import com.example.photobook.converterToDto.PhotoDtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhotobookConfig {

    @Bean
    public ModelMapper defaultModelMapper(AlbumDtoConverter albumDtoConverter,
                                          PhotoDtoConverter photoDtoConverter) {
        var modelMapper = new ModelMapper();
        modelMapper.addConverter(albumDtoConverter);
        modelMapper.addConverter(photoDtoConverter);
        return modelMapper;
    }
}
