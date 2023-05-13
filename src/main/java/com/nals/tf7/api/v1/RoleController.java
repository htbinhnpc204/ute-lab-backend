package com.nals.tf7.api.v1;

import com.nals.tf7.service.v1.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController
    extends BaseController {

    private final RoleService roleService;

    public RoleController(final Validator validator, final RoleService roleService) {
        super(validator);
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<?> fetchAll() {
        return ok(roleService.fetchAll());
    }
}
