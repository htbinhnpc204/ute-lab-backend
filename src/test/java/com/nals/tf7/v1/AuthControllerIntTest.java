package com.nals.tf7.v1;

import com.nals.tf7.AbstractTest;
import com.nals.tf7.Review360App;
import com.nals.tf7.api.v1.AuthController;
import com.nals.tf7.blueprints.UserBlueprint;
import com.nals.tf7.domain.Permission;
import com.nals.tf7.domain.Role;
import com.nals.tf7.domain.RolePermission;
import com.nals.tf7.domain.User;
import com.nals.tf7.dto.v1.request.auth.LoginReq;
import com.nals.tf7.helpers.TestHelper;
import com.tobedevoured.modelcitizen.CreateModelException;
import com.tobedevoured.modelcitizen.RegisterBlueprintException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.nals.tf7.helpers.TestHelper.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Review360App.class)
public class AuthControllerIntTest
    extends AbstractTest {

    @Autowired
    private AuthController authController;

    private User user;
    private MockMvc restMvc;
    private String baseUrl;

    @Before
    public void setup()
        throws RegisterBlueprintException, CreateModelException {
        this.restMvc = MockMvcBuilders.standaloneSetup(authController)
                                      .setMessageConverters(getHttpMessageConverters())
                                      .setControllerAdvice(getExceptionTranslator())
                                      .build();
        this.baseUrl = "/api/v1/auth";

        fakeData();
    }

    @Test
    @Transactional
    public void test_login_shouldBeOk()
        throws Exception {
        LoginReq dto = new LoginReq(user.getEmail(), ACCOUNT_PASSWORD);

        String imageUrl = getFileService().getFullFileUrl(user.getEmail());

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(dto)))
               .andExpect(status().isNoContent());
    }

    private void fakeData()
        throws RegisterBlueprintException, CreateModelException {
        registerBlueprints(UserBlueprint.class);

        Long permissionId = getPermissionRepository().save(new Permission("READ")).getId();
        Long roleId = getRoleRepository().save(new Role("ROLE_REQUESTER")).getId();

        user = createFakeModel(User.class);
        user.setPassword(getPasswordEncoder().encode(ACCOUNT_PASSWORD));
        user.setRole(Role.builder().id(roleId).build());
        getUserRepository().save(user);

        getRolePermissionRepository().save(RolePermission.builder()
                                                         .roleId(roleId)
                                                         .permissionId(permissionId)
                                                         .build());
    }
}
