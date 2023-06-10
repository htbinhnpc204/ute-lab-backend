package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.ComputerErrorBloc;
import com.nals.tf7.dto.v1.request.ComputerErrorReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

@RestController
@RequestMapping("api/v1/computers/{computerId}/errors")
public class ComputerErrorController
    extends BaseController {

    private final ComputerErrorBloc computerErrorBloc;

    public ComputerErrorController(final Validator validator, final ComputerErrorBloc computerErrorBloc) {
        super(validator);
        this.computerErrorBloc = computerErrorBloc;
    }

    @GetMapping()
    public ResponseEntity<?> getAllByComputer(@PathVariable final Long computerId) {
        return ok(computerErrorBloc.getAllErrorsByComputer(computerId));
    }

    @PostMapping()
    public ResponseEntity<?> createComputerError(@PathVariable final Long computerId,
                                                 @RequestBody final ComputerErrorReq req) {
        return ok(computerErrorBloc.create(computerId, req));
    }
}
