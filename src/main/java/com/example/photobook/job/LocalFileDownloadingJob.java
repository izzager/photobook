package com.example.photobook.job;

import com.example.photobook.entity.Photo;
import com.example.photobook.util.DownloadingStatusHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

@Configuration
@EnableScheduling
@Profile("!test")
public class LocalFileDownloadingJob {

    private final DownloadingStatusHelper downloadingStatusHelper;
    private final String pathToFiles;
    private final Long timeInterval;
    private final WebClient webClient;

    public LocalFileDownloadingJob(DownloadingStatusHelper downloadingStatusHelper,
                                   WebClient webClient,
                                   @Value("${photobookapp.downloading-directory}") String pathToFiles,
                                   @Value("${photobookapp.file-download-check-interval}") Long timeInterval) {
        this.downloadingStatusHelper = downloadingStatusHelper;
        this.webClient = webClient;
        this.pathToFiles = pathToFiles;
        this.timeInterval = timeInterval;
    }

    @Scheduled(fixedRateString = "${photobookapp.file-download-check-interval}")
    public void downloadFilesFromUrl() throws IOException {
        List<Photo> notDownloadedFiles = downloadingStatusHelper.findNotDownloadedFiles(timeInterval);
        for (Photo photo : notDownloadedFiles) {
            Path path = Paths.get(pathToFiles, photo.getPhotoName());
            Flux<DataBuffer> dataBufferFlux = webClient
                    .get()
                    .uri(photo.getLoadSource())
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
