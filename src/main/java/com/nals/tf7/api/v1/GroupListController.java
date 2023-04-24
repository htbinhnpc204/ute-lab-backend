package com.nals.tf7.api.v1;

import com.nals.tf7.bloc.v1.GroupListBloc;
import com.nals.tf7.dto.v1.request.GroupSearchReq;
import com.nals.tf7.helpers.JsonHelper;
import com.nals.tf7.helpers.PaginationHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupListController
    extends BaseController {

    private final GroupListBloc groupCrudBloc;

    public GroupListController(final Validator validator, final GroupListBloc groupCrudBloc) {
        super(validator);
        this.groupCrudBloc = groupCrudBloc;
    }

    @GetMapping
    public ResponseEntity<?> searchGroups(@RequestParam final Map<String, Object> req) {
        var groupSearchReq = JsonHelper.MAPPER.convertValue(req, GroupSearchReq.class);
        return ok(groupCrudBloc.searchGroups(
            PaginationHelper.generatePageRequest(groupSearchReq), groupSearchReq.getName()));
    }
}
