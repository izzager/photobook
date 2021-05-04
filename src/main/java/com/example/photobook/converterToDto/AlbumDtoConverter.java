package com.example.photobook.converterToDto;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.entity.Album;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AlbumDtoConverter implements Converter<Album, AlbumDto> {

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public AlbumDto convert(MappingContext<Album, AlbumDto> mappingContext) {
        AlbumDto albumDto = mappingContext.getDestination();
        Album album = mappingContext.getSource();
        if (albumDto == null) {
            albumDto = new AlbumDto();
        }
        modelMapper.map(album, albumDto);
        albumDto.setUserOwnerId(album.getUserOwner().getId());
        return albumDto;
    }
}
