package com.example.photobook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDto {

    private Long userId;

    private String username;

    private LocalDateTime lastActivityDate;

}
