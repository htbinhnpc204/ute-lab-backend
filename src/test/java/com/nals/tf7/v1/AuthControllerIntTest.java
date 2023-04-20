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
import com.nals.tf7.helpers.StringHelper;
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

    private static final String READ_PERMISSION = "READ";
    private static final String ROLE_REQUESTER = "ROLE_REQUESTER";

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

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(dto)))
               .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void test_login_withWrongEmailPattern_shouldBeBadRequest()
        throws Exception {
        LoginReq dto = new LoginReq(getFaker().lorem().word(), ACCOUNT_PASSWORD);

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(dto)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_login_withEmailIsEmpty_shouldBeBadRequest()
        throws Exception {
        LoginReq dto = new LoginReq(StringHelper.EMPTY, ACCOUNT_PASSWORD);

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(dto)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_login_withPasswordIsEmpty_shouldBeBadRequest()
        throws Exception {
        LoginReq dto = new LoginReq(user.getEmail(), StringHelper.EMPTY);

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(dto)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_login_withEmailPasswordIsIncorrect_shouldBeBadRequest()
        throws Exception {
        LoginReq dto = new LoginReq(user.getEmail(), ACCOUNT_PASSWORD + getFaker().lorem().word());

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/login")
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(dto)))
               .andExpect(status().isUnauthorized());
    }

    private void fakeData()
        throws RegisterBlueprintException, CreateModelException {
        registerBlueprints(UserBlueprint.class);

        Long permissionId = getPermissionRepository().save(new Permission(READ_PERMISSION)).getId();
        Role role = getRoleRepository().save(new Role(ROLE_REQUESTER));

        user = createFakeModel(User.class);
        user.setPassword(getPasswordEncoder().encode(ACCOUNT_PASSWORD));
        user.setRole(role);
        getUserRepository().save(user);

        getRolePermissionRepository().save(RolePermission.builder()
                                                         .roleId(role.getId())
                                                         .permissionId(permissionId)
                                                         .build());
    }
}
