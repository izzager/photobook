package com.example.photobook.service;

import com.example.photobook.dto.AlbumDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbums();
    AlbumDto createAlbum(AlbumDto albumDto);
    AlbumDto deleteAlbum(Long albumId);
    void downloadAsZip(Long albumId, HttpServletResponse response);
}
