package com.example.photobook.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.time.Duration;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

@Component
@RequiredArgsConstructor
public class LoadingPhotoByURLHelper {

    private final WebClient webClient;

    public File downloadPhotoFromUrl(String link, Path path) throws IOException {
        Flux<DataBuffer> dataBufferFlux = webClient
                .get()
                .uri(link)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToFlux(DataBuffer.class);
        return saveFileOnServer(path, dataBufferFlux);
    }

    private File saveFileOnServer(Path path, Flux<DataBuffer> dataBufferFlux) throws IOException {
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, CREATE, WRITE);
        Disposable downloading = DataBufferUtils.write(dataBufferFlux, asynchronousFileChannel)
                .timeout(Duration.ofSeconds(10))
                .subscribe();
        while (!downloading.isDisposed()) { }
        return new File(String.valueOf(path));
    }

}
