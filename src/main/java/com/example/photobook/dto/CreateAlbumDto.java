package com.example.photobook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateAlbumDto {

    @NotBlank(message = "Album name must be not blank")
    private String albumName;

}
