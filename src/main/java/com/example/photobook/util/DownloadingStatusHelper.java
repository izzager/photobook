package com.example.photobook.util;

import com.example.photobook.entity.Photo;
import com.example.photobook.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DownloadingStatusHelper {

    private final PhotoService photoService;

    @Value("${photobookapp.downloading-directory}")
    private String pathToFiles;

    @Value("${photobookapp.file-download-check-interval}")
    private Long timeInterval;

    public Optional<File> ensureFileIsDownloaded(String fileName) {
        File file = new File(pathToFiles + File.separator + fileName);
        if (file.exists()) {
            return Optional.of(file);
        } else {
            return Optional.empty();
        }
    }

    public List<Optional<File>> ensureFilesIsDownloaded(List<String> fileNames) {
        return fileNames.stream()
                .map(this::ensureFileIsDownloaded)
                .collect(Collectors.toList());
    }

    public List<Photo> findNotDownloadedFiles() {
        return photoService.findLastPhotos(timeInterval)
                .stream()
                .filter(photo -> !isFileDownloaded(photo.getPhotoName()))
                .collect(Collectors.toList());
    }

    private boolean isFileDownloaded(String fileName) {
        return Files.exists(Paths.get(pathToFiles + File.separator + fileName));
    }

}
