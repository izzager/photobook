package com.example.photobook.repository;

import com.example.photobook.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByLoadDateAfter(LocalDateTime dateTime);
    List<Photo> findAllByAlbumId(Long albumId);
}
