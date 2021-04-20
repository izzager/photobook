package com.example.photobook.controller;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/albums")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    public List<AlbumDto> findAllAlbums() {
        return albumService.findAllAlbums();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }

    @PostMapping
    public AlbumDto createAlbum(@Validated @RequestBody CreateAlbumDto albumDto) {
        return albumService.createAlbum(albumDto);
    }

    @GetMapping("{id}/download")
    public ResponseEntity<Resource> downloadAlbum(@PathVariable Long id) {
        InputStreamResource resource = albumService.downloadAsZip(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + albumService.findAlbumName(id) + ".zip");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
