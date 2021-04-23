package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbums();
    AlbumDto createAlbum(CreateAlbumDto albumDto);
    void deleteAlbum(Long albumId);
    File downloadAsZip(Long albumId) throws IOException;
    String findAlbumName(Long albumId);
}
