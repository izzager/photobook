package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbums();
    AlbumDto createAlbum(CreateAlbumDto albumDto);
    void deleteAlbum(Long albumId);
    InputStreamResource downloadAsZip(Long albumId);
    String findAlbumName(Long albumId);
}
