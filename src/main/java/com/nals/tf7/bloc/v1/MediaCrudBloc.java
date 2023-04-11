package com.nals.tf7.bloc.v1;

import com.nals.tf7.domain.Media;
import com.nals.tf7.dto.v1.response.MediaRes;
import com.nals.tf7.enums.MediaType;
import com.nals.tf7.errors.ObjectNotFoundException;
import com.nals.tf7.helpers.StringHelper;
import com.nals.tf7.service.v1.FileService;
import com.nals.tf7.service.v1.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.nals.tf7.errors.ErrorCodes.MEDIA_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaCrudBloc {

    private final FileService fileService;
    private final MediaService mediaService;

    @Transactional
    public MediaRes uploadMedia(final MultipartFile file)
        throws IOException {
        log.info("Upload media");

        fileService.validateFile(file);

        return MediaRes.builder()
                       .storedKey(fileService.uploadFile(file.getOriginalFilename(), file.getBytes()))
                       .build();
    }

    @Transactional
    public void saveMedia(final String fileName, final Long sourceId, final MediaType mediaType) {
        log.info("Save media with fileName #{}, sourceId #{} and type #{}", fileName, sourceId, mediaType);
        if (StringHelper.isBlank(fileName)) {
            return;
        }

        mediaService.save(Media.builder()
                               .sourceId(sourceId)
                               .storedKey(fileName)
                               .type(mediaType)
                               .build());

        fileService.saveFile(fileName);
    }

    @Transactional
    public void replaceMedia(final String newFileName, final String oldFileName,
                             final Long sourceId, final MediaType mediaType) {
        log.info("Replace media with oldFileName #{}, newFileName #{}", oldFileName, newFileName);

        if (Objects.equals(oldFileName, newFileName)) {
            return;
        }

        if (StringHelper.isBlank(newFileName)) {
            mediaService.deleteBySourceIdAndType(sourceId, mediaType);
            fileService.deleteFile(oldFileName);
            return;
        }

        Media media = mediaService.getBySourceIdAndMediaType(sourceId, mediaType).orElse(null);

        if (media == null) {
            mediaService.save(Media.builder()
                                   .sourceId(sourceId)
                                   .storedKey(newFileName)
                                   .type(mediaType)
                                   .build());

            fileService.saveFile(newFileName);
        } else {
            media.setStoredKey(newFileName);
            mediaService.save(media);

            fileService.replaceFile(newFileName, oldFileName);
        }
    }

    @Transactional
    public void deleteMedia(final Long id) {
        log.info("Delete media by id #{}", id);

        Media media = mediaService.getById(id)
                                  .orElseThrow(() -> new ObjectNotFoundException("media", MEDIA_NOT_FOUND));

        mediaService.delete(media);
        fileService.deleteFile(media.getStoredKey());
    }
}
