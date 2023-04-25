package com.nals.tf7.bloc.v1;

import com.nals.tf7.dto.v1.response.MediaRes;
import com.nals.tf7.service.v1.FileService;
import com.nals.tf7.service.v1.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaCrudBloc {

    private final FileService fileService;
    private final MinioService minioService;

    @Transactional
    public MediaRes uploadMedia(final MultipartFile file)
        throws IOException {
        String fileName = file.getOriginalFilename();
        log.info("Upload media, #{}", fileName);

        fileService.validateFile(file);

        String uploadedFileName = minioService.uploadFile(fileService.generateFileName(fileName),
                                                          file.getInputStream(),
                                                          file.getSize());

        return MediaRes.builder()
                       .storedKey(uploadedFileName)
                       .build();
    }
}
