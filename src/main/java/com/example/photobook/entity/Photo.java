package com.example.photobook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoName;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    private String loadSource;

    @CreationTimestamp
    private LocalDateTime loadDate;
}
