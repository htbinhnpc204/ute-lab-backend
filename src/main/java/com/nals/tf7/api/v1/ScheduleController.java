package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.ScheduleBloc;
import com.nals.tf7.dto.v1.request.ScheduleReq;
import com.nals.tf7.dto.v1.request.ScheduleSearchReq;
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
@RequestMapping("/api/v1/schedules")
public class ScheduleController
    extends BaseController {

    private final ScheduleBloc scheduleBloc;

    public ScheduleController(final Validator validator, final ScheduleBloc scheduleBloc) {
        super(validator);
        this.scheduleBloc = scheduleBloc;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final ScheduleReq req) {
        return ok(scheduleBloc.createSchedule(req));
    }

    @GetMapping
    public ResponseEntity<?> fetchAll(@RequestParam final Map<String, Object> req) {
        var searchReq = JsonHelper.convertValue(req, ScheduleSearchReq.class);
        return ok(scheduleBloc.searchByLabAndClass(searchReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable final Long id) {
        return ok(scheduleBloc.getById(id));
    }

    @GetMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable final Long id) {
        scheduleBloc.approveSchedule(id);
        return noContent();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody final ScheduleReq req) {
        return ok(scheduleBloc.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        scheduleBloc.deleteSchedule(id);
        return noContent();
    }
}
