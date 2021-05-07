package com.example.photobook.configuration;

import com.example.photobook.converterToDto.AlbumDtoConverter;
import com.example.photobook.converterToDto.PhotoDtoConverter;
import com.example.photobook.security.AdminFilter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
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

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilter() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminFilter());
        registrationBean.addUrlPatterns("/admin/*");
        return registrationBean;
    }
}
