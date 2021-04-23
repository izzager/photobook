package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface AlbumService {
    List<AlbumDto> findAllAlbums();
    AlbumDto createAlbum(CreateAlbumDto albumDto);
    void deleteAlbum(Long albumId);
    Optional<File> downloadAsZip(Long albumId);
    String findAlbumName(Long albumId);
}
