package com.example.photobook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformUsageDto {

    private String platformName;

    private Long numberOfUses;

}
