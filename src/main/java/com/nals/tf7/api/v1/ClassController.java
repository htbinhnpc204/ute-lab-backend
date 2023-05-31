package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.ClassBloc;
import com.nals.tf7.bloc.v1.ScheduleBloc;
import com.nals.tf7.dto.v1.request.ClassReq;
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
@RequestMapping("/api/v1/classes")
public class ClassController
    extends BaseController {

    private final ClassBloc classBloc;
    private final ScheduleBloc scheduleBloc;

    public ClassController(final Validator validator, final ClassBloc classBloc, final ScheduleBloc scheduleBloc) {
        super(validator);
        this.classBloc = classBloc;
        this.scheduleBloc = scheduleBloc;
    }

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody final ClassReq req) {
        return created(classBloc.createClass(req).getId());
    }

    @GetMapping
    public ResponseEntity<?> fetchAllClass(@RequestParam final Map<String, Object> req) {
        var searchReq = JsonHelper.convertValue(req, SearchReq.class);
        return ok(classBloc.searchClasses(searchReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneClass(@PathVariable final Long id) {
        return ok(classBloc.getById(id));
    }

    @GetMapping("/{id}/schedules")
    public ResponseEntity<?> getScheduleByClass(@PathVariable final Long id) {
        return ok(scheduleBloc.searchByClass(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable final Long id, @RequestBody final ClassReq labReq) {
        return ok(classBloc.update(id, labReq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable final Long id) {
        if (id.equals(classBloc.deleteClass(id))) {
            return noContent();
        }

        return ResponseEntity.badRequest().build();
    }
}
