package com.example.photobook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadPhotoDto {

    @NotBlank(message = "Photo name must be not blank")
    private String photoName;

    @Null(message = "albumId parameter not allowed")
    private Long albumId;

    private String link;

    @Null(message = "Username must be null")
    private String username;

}
