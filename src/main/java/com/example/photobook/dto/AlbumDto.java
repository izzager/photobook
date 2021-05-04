package com.example.photobook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumDto {

    private Long id;

    private String albumName;

    private Long userOwnerId;
}
