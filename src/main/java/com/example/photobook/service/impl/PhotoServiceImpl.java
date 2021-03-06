package com.example.photobook.service.impl;

import com.example.photobook.dto.PhotoDto;
import com.example.photobook.dto.UploadPhotoDto;
import com.example.photobook.entity.Photo;
import com.example.photobook.exception.ResourceForbiddenException;
import com.example.photobook.helper.PhotoRepositoryHelper;
import com.example.photobook.mapperToEntity.UploadPhotoDtoMapper;
import com.example.photobook.repository.AlbumRepository;
import com.example.photobook.repository.PhotoRepository;
import com.example.photobook.security.UserContext;
import com.example.photobook.service.PhotoService;
import com.example.photobook.util.DownloadingStatusHelper;
import com.example.photobook.util.LoadingPhotoByURLHelper;
import com.example.photobook.validator.UploadPhotoDtoValidator;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static com.example.photobook.util.PhotoUtils.buildPhotoName;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final UploadPhotoDtoMapper uploadPhotoDtoMapper;
    private final UploadPhotoDtoValidator uploadPhotoDtoValidator;
    private final PhotoRepositoryHelper photoRepositoryHelper;
    private final DownloadingStatusHelper downloadingStatusHelper;
    private final LoadingPhotoByURLHelper loadingPhotoByURLHelper;
    private final UserContext userContext;
    private final AlbumRepository albumRepository;
    private final String pathToFiles;

    public PhotoServiceImpl(PhotoRepository photoRepository,
                            ModelMapper modelMapper,
                            UploadPhotoDtoMapper uploadPhotoDtoMapper,
                            UploadPhotoDtoValidator uploadPhotoDtoValidator,
                            PhotoRepositoryHelper photoRepositoryHelper,
                            DownloadingStatusHelper downloadingStatusHelper,
                            LoadingPhotoByURLHelper loadingPhotoByURLHelper,
                            UserContext userContext,
                            AlbumRepository albumRepository,
                            @Value("${photobookapp.downloading-directory}") String pathToFiles) {
        this.photoRepository = photoRepository;
        this.modelMapper = modelMapper;
        this.uploadPhotoDtoMapper = uploadPhotoDtoMapper;
        this.uploadPhotoDtoValidator = uploadPhotoDtoValidator;
        this.photoRepositoryHelper = photoRepositoryHelper;
        this.downloadingStatusHelper = downloadingStatusHelper;
        this.loadingPhotoByURLHelper = loadingPhotoByURLHelper;
        this.userContext = userContext;
        this.albumRepository = albumRepository;
        this.pathToFiles = pathToFiles;
    }

    @Override
    public void deletePhoto(Long photoId) {
        Optional<Photo> photoOptional = photoRepository.findById(photoId);
        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            if (!photo.getAlbum().getUserOwner().getUsername()
                    .equals(userContext.getAuthentication().getName())) {
                throw new ResourceForbiddenException("You are not owner of this photo");
            }
            String pathToPhoto = pathToFiles + File.separator + photoOptional.get().getPhotoName();
            photoRepository.deleteById(photoId);
            new File(pathToPhoto).delete();
        }
    }

    @Override
    public File findPhotoById(Long albumId, Long photoId) throws IOException {
        Photo photo = photoRepositoryHelper.ensurePhotoExists(albumId, photoId);
        Optional<File> file = downloadingStatusHelper.findLocalFileInstance(photo.getPhotoName());
        if (file.isPresent()) {
            return file.get();
        } else {
            return loadingPhotoByURLHelper
                        .downloadPhotoFromUrl(photo.getLoadSource(), Paths.get(pathToFiles, photo.getPhotoName()));
        }
    }

    @Override
    public PhotoDto uploadPhotoFromComputer(UploadPhotoDto uploadPhotoDto,
                                            MultipartFile file) throws IOException {
        checkUserRights(uploadPhotoDto.getAlbumId());
        uploadPhotoDtoValidator.checkPhotoUploadingFromComputer(uploadPhotoDto, file);
        uploadPhotoDto.setPhotoName(buildPhotoName(uploadPhotoDto, file));
        savePhotoOnServer(uploadPhotoDto, file);
        Photo savedPhoto = photoRepository.save(uploadPhotoDtoMapper.toEntity(uploadPhotoDto));
        return modelMapper.map(savedPhoto, PhotoDto.class);
    }

    @Override
    public PhotoDto uploadPhotoByUrl(UploadPhotoDto uploadPhotoDto) {
        checkUserRights(uploadPhotoDto.getAlbumId());
        uploadPhotoDtoValidator.checkPhotoUploadingByUrl(uploadPhotoDto);
        uploadPhotoDto.setPhotoName(buildPhotoName(uploadPhotoDto));
        Photo savedPhoto = photoRepository.save(uploadPhotoDtoMapper.toEntity(uploadPhotoDto));
        return modelMapper.map(savedPhoto, PhotoDto.class);
    }

    private void savePhotoOnServer(UploadPhotoDto uploadPhotoDto, MultipartFile file) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        File uploadedFile = new File(pathToFiles + File.separator + uploadPhotoDto.getPhotoName());
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    private void checkUserRights(Long albumId) {
        if (!albumRepository.existsAlbumByIdAndUserOwnerUsername(albumId,
                userContext.getAuthentication().getName())) {
            throw new ResourceForbiddenException("You are not the owner of this album");
        }
    }

}
