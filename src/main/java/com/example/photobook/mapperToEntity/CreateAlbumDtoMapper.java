package com.example.photobook.mapperToEntity;

import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.entity.Album;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAlbumDtoMapper implements Mapper<CreateAlbumDto, Album> {

    private final ModelMapper modelMapper;

    public Album toEntity(CreateAlbumDto albumDto) {
        Album album = new Album();
        modelMapper.map(albumDto, album);
        return album;
    }
}
