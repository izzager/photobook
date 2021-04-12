package com.example.photobook.service.impl;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.service.AlbumService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {
    @Override
    public List<AlbumDto> findAllAlbum() {
        return null;
    }

    @Override
    public AlbumDto createAlbum(AlbumDto albumDto) {
        return null;
    }

    @Override
    public AlbumDto deleteAlbum(Long albumId) {
        return null;
    }

    @Override
    public void downloadAsZip(Long albumId) {

    }
}
