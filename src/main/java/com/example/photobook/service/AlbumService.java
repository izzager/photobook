package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;

import javax.servlet.ServletOutputStream;
import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbums();
    AlbumDto createAlbum(AlbumDto albumDto);
    void deleteAlbum(Long albumId);
    void downloadAsZip(Long albumId, ServletOutputStream servletOutputStream);
    String findAlbumName(Long albumId);
}
