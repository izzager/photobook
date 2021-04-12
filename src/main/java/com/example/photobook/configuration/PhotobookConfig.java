package com.example.photobook.configuration;

import com.example.photobook.converterToDto.AlbumConverter;
import com.example.photobook.converterToDto.PhotoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhotobookConfig {

    @Bean
    public ModelMapper defaultModelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.addConverter(new AlbumConverter());
        modelMapper.addConverter(new PhotoConverter());
        return modelMapper;
    }
}
