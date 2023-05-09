package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.LabBloc;
import com.nals.tf7.dto.v1.request.LabReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.helpers.JsonHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/labs")
public class LabController
    extends BaseController {

    private final LabBloc labBloc;

    public LabController(final Validator validator, final LabBloc labBloc) {
        super(validator);
        this.labBloc = labBloc;
    }

    @PostMapping
    public ResponseEntity<?> createLab(@ModelAttribute final LabReq req) {
        return created(labBloc.createLab(req).getId());
    }

    @GetMapping
    public ResponseEntity<?> fetchAllLab(@RequestParam final Map<String, Object> req) {
        var searchReq = JsonHelper.convertValue(req, SearchReq.class);
        return ok(labBloc.searchLabs(searchReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneLab(@PathVariable final Long id) {
        return ok(labBloc.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLab(@PathVariable final Long id, @ModelAttribute final LabReq labReq) {
        labBloc.update(id, labReq);
        return noContent();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLab(@PathVariable final Long id) {
        if (id.equals(labBloc.deleteLab(id).getId())) {
            return noContent();
        }

        return ResponseEntity.badRequest().build();
    }
}
