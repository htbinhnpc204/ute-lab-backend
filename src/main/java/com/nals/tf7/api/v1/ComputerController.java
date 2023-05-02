package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.ComputerBloc;
import com.nals.tf7.dto.v1.request.ComputerReq;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.helpers.JsonHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/computers")
public class ComputerController
    extends BaseController {

    private final ComputerBloc computerBloc;

    public ComputerController(final Validator validator, final ComputerBloc computerBloc) {
        super(validator);
        this.computerBloc = computerBloc;
    }

    @PostMapping
    public ResponseEntity<?> createComputer(@RequestBody final ComputerReq req) {
        return created(computerBloc.createComputer(req).getId());
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateActivateStatus(@PathVariable final Long id) {
        if (computerBloc.updateStatus(id).getId().equals(id)) {
            return noContent();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<?> fetchAllComputer(@RequestParam final Map<String, Object> req) {
        var searchReq = JsonHelper.convertValue(req, SearchReq.class);
        return ok(computerBloc.searchComputers(searchReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneComputer(@PathVariable final Long id) {
        return ok(computerBloc.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLab(@PathVariable final Long id, @RequestBody final ComputerReq req) {
        return ok(computerBloc.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLab(@PathVariable final Long id) {
        if (id.equals(computerBloc.deleteLab(id).getId())) {
            return noContent();
        }

        return ResponseEntity.badRequest().build();
    }
}
