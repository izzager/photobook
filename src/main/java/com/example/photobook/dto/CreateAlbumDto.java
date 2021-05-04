package com.example.photobook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
public class CreateAlbumDto {

    @NotBlank(message = "Album name must be not blank")
    private String albumName;

    @Null(message = "UserId must be null")
    private Long userOwnerId;

}
