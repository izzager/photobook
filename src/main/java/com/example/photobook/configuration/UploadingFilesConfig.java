package com.example.photobook.configuration;

import com.example.photobook.entity.Photo;
import com.example.photobook.util.DownloadingStatusHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class UploadingFilesConfig {

    private final DownloadingStatusHelper downloadingStatusHelper;

    @Value("${photobookapp.downloading-directory}")
    private String pathToFiles;

    @Scheduled(fixedRateString = "${photobookapp.file-download-check-interval}")
    public void downloadFilesFromUrl() throws IOException {
        List<Photo> notDownloadedFiles = downloadingStatusHelper.findNotDownloadedFiles();
        for (Photo photo : notDownloadedFiles) {
            Path path = Paths.get(pathToFiles + File.separator + photo.getPhotoName());
            WebClient client = WebClient.builder()
                    .baseUrl(photo.getLoadSource())
                    .build();
            Flux<DataBuffer> dataBufferFlux = client
                    .get()
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .bodyToFlux(DataBuffer.class);
            saveFileOnServer(path, dataBufferFlux);
        }
    }

    private void saveFileOnServer(Path path, Flux<DataBuffer> dataBufferFlux) throws IOException {
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, CREATE, WRITE);
        DataBufferUtils.write(dataBufferFlux, asynchronousFileChannel).subscribe();
    }
}
