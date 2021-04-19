package com.example.photobook.service.impl;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.entity.Album;
import com.example.photobook.helper.AlbumRepositoryHelper;
import com.example.photobook.mapperToEntity.AlbumMapper;
import com.example.photobook.repository.AlbumRepository;
import com.example.photobook.service.AlbumService;
import com.example.photobook.util.FileZipper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepositoryHelper albumRepositoryHelper;
    private final AlbumMapper albumMapper;

    @Value("${photobookapp.downloading-directory}")
    private String pathToFiles;

    @Override
    public List<AlbumDto> findAllAlbums() {
        return albumRepository.findAll()
                .stream()
                .map(album -> modelMapper.map(album, AlbumDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AlbumDto createAlbum(AlbumDto albumDto) {
        Album album = albumMapper.toEntity(albumDto);
        return modelMapper.map(albumRepository.save(album), AlbumDto.class);
    }

    @Override
    public void deleteAlbum(Long albumId) {
        albumRepository
                .findById(albumId)
                .ifPresent(albumRepository::delete);
    }

    @Override
    public void downloadAsZip(Long albumId, ServletOutputStream servletOutputStream) {
        Album album = albumRepositoryHelper.ensureAlbumExists(albumId);
        List<String> files = album.getPhotos()
                .stream()
                .map(photo -> pathToFiles + photo.getPhotoName())
                .collect(Collectors.toList());
        FileZipper.zip(files, servletOutputStream);
    }

    @Override
    public String findAlbumName(Long albumId) {
        return albumRepositoryHelper.ensureAlbumExists(albumId).getAlbumName();
    }
}
