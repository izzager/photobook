package com.example.photobook.service.impl;

import com.example.photobook.dto.AlbumDto;
import com.example.photobook.entity.Album;
import com.example.photobook.entity.Photo;
import com.example.photobook.helper.AlbumRepositoryHelper;
import com.example.photobook.mapperToEntity.AlbumMapper;
import com.example.photobook.repository.AlbumRepository;
import com.example.photobook.util.FileZipper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.servlet.ServletOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.photobook.TestConstants.ALBUM_ID;
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
    private AlbumMapper albumMapper;

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
        AlbumDto albumDto = new AlbumDto();
        Album album = new Album();

        when(albumMapper.toEntity(albumDto)).thenReturn(album);
        when(albumRepository.save(album)).thenReturn(album);
        albumService.createAlbum(albumDto);

        verify(albumMapper).toEntity(albumDto);
        verify(albumRepository).save(album);
        verify(modelMapper).map(album, AlbumDto.class);
    }

    @Test
    public void deleteAlbum_albumExists_passes() {
        AlbumDto albumDto = new AlbumDto();
        Album album = new Album();

        when(albumRepositoryHelper.ensureAlbumExists(ALBUM_ID)).thenReturn(album);
        when(modelMapper.map(album, AlbumDto.class)).thenReturn(albumDto);
        albumService.deleteAlbum(ALBUM_ID);

        verify(albumRepository).delete(album);
        verify(albumRepositoryHelper).ensureAlbumExists(ALBUM_ID);
        verify(modelMapper).map(album, AlbumDto.class);
    }

    @Test
    public void downloadAsZip_passes() {
        ServletOutputStream servletOutputStream = Mockito.mock(ServletOutputStream.class);
        Album album = new Album();
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo());
        album.setPhotos(photos);
        Mockito.mockStatic(FileZipper.class);

        when(albumRepositoryHelper.ensureAlbumExists(ALBUM_ID)).thenReturn(album);
        albumService.downloadAsZip(ALBUM_ID, servletOutputStream);

        verify(albumRepositoryHelper).ensureAlbumExists(ALBUM_ID);
    }

}
