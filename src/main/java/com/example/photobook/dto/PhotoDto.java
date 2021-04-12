package com.example.photobook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PhotoDto {
    private Long id;

    private String photoName;

    private Long albumId;

    private LocalDateTime loadDate;

    private String link;
}
