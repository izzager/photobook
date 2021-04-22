package com.example.photobook.service.impl;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.entity.Album;
import com.example.photobook.helper.AlbumRepositoryHelper;
import com.example.photobook.mapperToEntity.CreateAlbumDtoMapper;
import com.example.photobook.repository.AlbumRepository;
import com.example.photobook.service.AlbumService;
import com.example.photobook.util.FileZipper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepositoryHelper albumRepositoryHelper;
    private final CreateAlbumDtoMapper createAlbumDtoMapper;

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
    public AlbumDto createAlbum(CreateAlbumDto albumDto) {
        Album album = createAlbumDtoMapper.toEntity(albumDto);
        return modelMapper.map(albumRepository.save(album), AlbumDto.class);
    }

    @Override
    public void deleteAlbum(Long albumId) {
        if (albumRepository.existsById(albumId)) {
            albumRepository.deleteById(albumId);
        }
    }

    @Override
    public File downloadAsZip(Long albumId) {
        Album album = albumRepositoryHelper.ensureAlbumExists(albumId);
        List<String> files = album.getPhotos()
                .stream()
                .map(photo -> pathToFiles + "/" + photo.getPhotoName())
                .collect(Collectors.toList());
        ByteArrayOutputStream zippedAlbum = FileZipper.zip(files);

        //write zipped album to File
        File file = null;
        try {
            file = File.createTempFile(album.getAlbumName(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (OutputStream outputStream = new FileOutputStream(file)) {
            zippedAlbum.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public String findAlbumName(Long albumId) {
        return albumRepositoryHelper.ensureAlbumExists(albumId).getAlbumName();
    }
}
