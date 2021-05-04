package com.example.photobook.mapperToEntity;

import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.User;
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
        User user = new User();
        user.setId(albumDto.getUserOwnerId());
        album.setUserOwner(user);
        return album;
    }
}
