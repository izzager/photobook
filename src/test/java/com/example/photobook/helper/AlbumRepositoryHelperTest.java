package com.example.photobook.helper;

import com.example.photobook.entity.Album;
import com.example.photobook.exception.ResourceNotFoundException;
import com.example.photobook.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.photobook.TestConstants.ALBUM_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumRepositoryHelperTest {

    @InjectMocks
    private AlbumRepositoryHelper albumRepositoryHelper;

    @Mock
    private AlbumRepository albumRepository;

    @Test
    public void ensureMovieExists_true() {
        Album album = new Album();
        album.setId(ALBUM_ID);

        when(albumRepository.findById(ALBUM_ID)).thenReturn(Optional.of(album));
        Album result = albumRepositoryHelper.ensureAlbumExists(ALBUM_ID);

        assertEquals(result.getId(), ALBUM_ID);
        verify(albumRepository).findById(ALBUM_ID);
    }

    @Test
    public void ensureMovieExists_false() {
        when(albumRepository.findById(ALBUM_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> albumRepositoryHelper.ensureAlbumExists(ALBUM_ID));
    }

}