package com.example.photobook.mapperToEntity;

import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.User;
import com.example.photobook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateAlbumDtoMapper implements Mapper<CreateAlbumDto, Album> {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public Album toEntity(CreateAlbumDto albumDto) {
        Album album = new Album();
        modelMapper.map(albumDto, album);
        Optional<User> userOptional = userRepository.findUserByUsername(albumDto.getUsername());
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        album.setUserOwner(userOptional.get());
        return album;
    }
}
