package com.nals.tf7.v1;

import com.nals.tf7.AbstractTest;
import com.nals.tf7.Review360App;
import com.nals.tf7.api.v1.AuthController;
import com.nals.tf7.api.v1.GroupListController;
import com.nals.tf7.blueprints.GroupBlueprint;
import com.nals.tf7.blueprints.UserBlueprint;
import com.nals.tf7.domain.Group;
import com.nals.tf7.domain.Permission;
import com.nals.tf7.domain.Role;
import com.nals.tf7.domain.RolePermission;
import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.auth.LoginReq;
import com.nals.tf7.dto.v1.response.DataRes;
import com.nals.tf7.dto.v1.response.OAuthTokenRes;
import com.nals.tf7.enums.GroupType;
import com.nals.tf7.security.jwt.JWTFilter;
import com.nals.tf7.security.jwt.TokenProvider;
import com.nals.tf7.service.v1.RedisService;
import com.tobedevoured.modelcitizen.CreateModelException;
import com.tobedevoured.modelcitizen.RegisterBlueprintException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Review360App.class)
public class GroupListControllerIntTest
    extends AbstractTest {

    private static final String INVALID_TOKEN = "invalid-token";
    public static final String ROLE_REQUESTER = "ROLE_BOD";
    public static final String PERMISSION_READ = "READ";

    @Autowired
    private GroupListController groupCrudController;

    @Autowired
    private AuthController authController;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private RedisService redisService;

    private User user;
    private MockMvc restMvc;
    private String baseUrl;

    @Before
    public void setup()
        throws RegisterBlueprintException, CreateModelException {
        this.restMvc = MockMvcBuilders.standaloneSetup(groupCrudController)
                                      .addFilter(new JWTFilter(tokenProvider, redisService))
                                      .setMessageConverters(getHttpMessageConverters())
                                      .setControllerAdvice(getExceptionTranslator())
                                      .build();
        this.baseUrl = "/api/v1/groups";

        fakeData();
    }

    @Test
    @Transactional
    public void test_searchGroups_shouldBeOk()
        throws Exception {
        LoginReq dto = new LoginReq(user.getEmail(), ACCOUNT_PASSWORD);

        ResponseEntity<?> request = authController.login(dto);

        DataRes dataRes = (DataRes) request.getBody();

        OAuthTokenRes auth = (OAuthTokenRes) dataRes.getData();

        restMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                                              .header("RW-Authorization", "Bearer " + auth.getAccessToken()))
               .andExpect(status().isOk());
    }

    private void fakeData()
        throws RegisterBlueprintException, CreateModelException {
        registerBlueprints(UserBlueprint.class);
        registerBlueprints(GroupBlueprint.class);

        Long permissionId = getPermissionRepository().save(new Permission(PERMISSION_READ)).getId();
        Long roleId = getRoleRepository().save(new Role(ROLE_REQUESTER)).getId();

        getRolePermissionRepository().save(RolePermission.builder()
                                                         .roleId(roleId)
                                                         .permissionId(permissionId)
                                                         .build());

        user = createFakeModel(User.class);
        user.setPassword(getPasswordEncoder().encode(ACCOUNT_PASSWORD));
        user.setRole(getRoleRepository().getById(roleId));
        getUserRepository().save(user);

        Group group = createFakeModel(Group.class);
        group.setType(GroupType.TECH);
        getGroupRepository().save(group);
    }
}
