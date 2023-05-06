package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.ClassUserBloc;
import com.nals.tf7.dto.v1.request.SearchReq;
import com.nals.tf7.helpers.JsonHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

import java.util.Map;

@RestController
@RequestMapping("api/v1/class/{classId}/users")
public class ClassUserController
    extends BaseController {

    private final ClassUserBloc classUserBloc;

    public ClassUserController(final Validator validator, final ClassUserBloc classUserBloc) {
        super(validator);
        this.classUserBloc = classUserBloc;
    }

    @PostMapping
    public ResponseEntity<?> addUserToClass(@PathVariable final Long classId, @RequestBody final Long userId) {
        return created(classUserBloc.addUserToClass(classId, userId));
    }

    @GetMapping
    public ResponseEntity<?> getClassUsers(@PathVariable final Long classId,
                                           @RequestParam final Map<String, Object> req) {
        var searchReq = JsonHelper.convertValue(req, SearchReq.class);
        return ok(classUserBloc.searchClassUsers(classId, searchReq));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> getClassUsers(@PathVariable final Long classId,
                                           @PathVariable final Long userId) {
        classUserBloc.deleteUserFromClass(classId, userId);
        return noContent();
    }
}
