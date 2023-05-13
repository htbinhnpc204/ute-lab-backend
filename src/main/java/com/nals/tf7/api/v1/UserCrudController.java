package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.UserCrudBloc;
import com.nals.tf7.dto.v1.request.UserReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
public class UserCrudController
    extends BaseController {

    private final UserCrudBloc userCrudBloc;

    public UserCrudController(final Validator validator, final UserCrudBloc userCrudBloc) {
        super(validator);
        this.userCrudBloc = userCrudBloc;
    }

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute final UserReq registerReq) {
        return created(userCrudBloc.create(registerReq));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateActivated(@PathVariable final Long id) {
        userCrudBloc.updateActivated(id);
        return noContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable final Long id) {
        return ok(userCrudBloc.getOneById(id));
    }

    @GetMapping
    public ResponseEntity<?> fetchAll() {
        return ok(userCrudBloc.fetchAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final Long id, @ModelAttribute final UserReq req) {
        return Objects.equals(userCrudBloc.update(id, req), id) ? noContent() : badRequest();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        userCrudBloc.delete(id);
        return noContent();
    }
}
