package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.MediaCrudBloc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/media")
public class MediaCrudController
    extends BaseController {

    private final MediaCrudBloc mediaCrudBloc;

    public MediaCrudController(final Validator validator,
                               final MediaCrudBloc mediaCrudBloc) {
        super(validator);
        this.mediaCrudBloc = mediaCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> uploadMedia(final MultipartFile file)
        throws IOException {
        return ok(mediaCrudBloc.uploadMedia(file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable final Long id) {
        mediaCrudBloc.deleteMedia(id);
        return noContent();
    }
}
