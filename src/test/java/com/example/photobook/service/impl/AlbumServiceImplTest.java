package com.example.photobook.service.impl;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.dto.CreateAlbumDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.Photo;
import com.example.photobook.helper.AlbumRepositoryHelper;
import com.example.photobook.mapperToEntity.CreateAlbumDtoMapper;
import com.example.photobook.repository.AlbumRepository;
import com.example.photobook.util.FileZipper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.photobook.TestConstants.ALBUM_ID;
import static com.example.photobook.TestConstants.ALBUM_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceImplTest {

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AlbumRepositoryHelper albumRepositoryHelper;

    @Mock
    private CreateAlbumDtoMapper createAlbumDtoMapper;

    @Test
    public void findAllAlbums_passes() {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album());

        Mockito.when(albumRepository.findAll()).thenReturn(albums);
        List<AlbumDto> result = albumService.findAllAlbums();

        assertEquals(result.size(), albums.size());
        verify(albumRepository).findAll();
    }

    @Test
    public void createAlbum_passes() {
        CreateAlbumDto albumDto = new CreateAlbumDto();
        Album album = new Album();

        when(createAlbumDtoMapper.toEntity(albumDto)).thenReturn(album);
        when(albumRepository.save(album)).thenReturn(album);
        albumService.createAlbum(albumDto);

        verify(createAlbumDtoMapper).toEntity(albumDto);
        verify(albumRepository).save(album);
        verify(modelMapper).map(album, AlbumDto.class);
    }

    @Test
    public void deleteAlbum_albumExists_passes() {
        when(albumRepository.existsById(ALBUM_ID)).thenReturn(true);
        albumService.deleteAlbum(ALBUM_ID);

        verify(albumRepository).existsById(ALBUM_ID);
        verify(albumRepository).deleteById(ALBUM_ID);
    }

    @Test
    public void downloadAsZip_passes() {
        Album album = new Album();
        album.setAlbumName(ALBUM_NAME);
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo());
        album.setPhotos(photos);
        List<String> files = new ArrayList<>();
        File file = null;
        try {
            file = File.createTempFile(album.getAlbumName(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (MockedStatic<FileZipper> fileZipperMockedStatic = Mockito.mockStatic(FileZipper.class)) {
            fileZipperMockedStatic.when((MockedStatic.Verification) FileZipper.zip(files)).thenReturn(new ByteArrayOutputStream(0));
        }
        when(albumRepositoryHelper.ensureAlbumExists(ALBUM_ID)).thenReturn(album);
        albumService.downloadAsZip(ALBUM_ID);

        verify(albumRepositoryHelper).ensureAlbumExists(ALBUM_ID);
    }

}
