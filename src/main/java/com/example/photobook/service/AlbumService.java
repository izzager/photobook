package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;

import javax.servlet.ServletOutputStream;
import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbums();
    AlbumDto createAlbum(CreateAlbumDto albumDto);
    void deleteAlbum(Long albumId);
    void downloadAsZip(Long albumId, ServletOutputStream servletOutputStream);
    String findAlbumName(Long albumId);
}
