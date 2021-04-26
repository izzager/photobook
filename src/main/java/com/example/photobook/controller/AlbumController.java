package com.example.photobook.controller;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    public ResponseEntity<byte[]> downloadAlbum(@PathVariable Long id,
                                                HttpServletResponse response) throws IOException {
        File file = albumService.downloadAsZip(id);
        response.addHeader("Content-Disposition",
                "attachment; filename=" + file.getName());
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), HttpStatus.OK);
    }

}
