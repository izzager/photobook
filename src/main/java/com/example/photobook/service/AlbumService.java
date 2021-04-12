package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;

import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbum();
    AlbumDto createAlbum(AlbumDto albumDto);
    AlbumDto deleteAlbum(Long albumId);
    void downloadAsZip(Long albumId);
}
