package com.example.photobook.controller;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                "attachment; filename=" + file.getName() + ".zip");
        return new ResponseEntity<>(FileUtil.readAsByteArray(file), HttpStatus.OK);
    }

}
