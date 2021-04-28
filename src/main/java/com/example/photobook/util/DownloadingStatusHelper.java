package com.example.photobook.util;

import com.example.photobook.entity.Photo;
import com.example.photobook.service.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DownloadingStatusHelper {

    private final PhotoService photoService;
    private final String pathToFiles;

    public DownloadingStatusHelper(PhotoService photoService,
                                   @Value("${photobookapp.downloading-directory}") String pathToFiles) {
        this.photoService = photoService;
        this.pathToFiles = pathToFiles;
    }

    public Optional<File> findLocalFileInstance(String fileName) {
        File file = new File(pathToFiles + File.separator + fileName);
        return Optional.of(file).filter(File::exists);
    }

    public List<Optional<File>> findLocalFileInstance(List<String> fileNames) {
        return fileNames.stream()
                .map(this::findLocalFileInstance)
                .collect(Collectors.toList());
    }

    public List<Photo> findNotDownloadedFiles(Long timeInterval) {
        return photoService.findLastPhotos(timeInterval)
                .stream()
                .filter(photo -> !isFileDownloaded(photo.getPhotoName()))
                .collect(Collectors.toList());
    }

    private boolean isFileDownloaded(String fileName) {
        return Files.exists(Paths.get(pathToFiles, fileName));
    }

}
