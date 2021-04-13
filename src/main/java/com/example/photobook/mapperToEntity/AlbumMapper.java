package com.example.photobook.mapperToEntity;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.entity.Album;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumMapper implements Mapper<AlbumDto, Album> {

    private final ModelMapper modelMapper;

    public Album toEntity(AlbumDto albumDto) {
        Album album = new Album();
        modelMapper.map(albumDto, album);
        return album;
    }
}
