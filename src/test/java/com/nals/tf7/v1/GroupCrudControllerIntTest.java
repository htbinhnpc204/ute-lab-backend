package com.nals.tf7.v1;

import com.nals.tf7.AbstractTest;
import com.nals.tf7.Review360App;
import com.nals.tf7.api.v1.GroupCrudController;
import com.nals.tf7.blueprints.GroupBlueprint;
import com.nals.tf7.domain.Group;
import com.nals.tf7.dto.v1.request.GroupReq;
import com.nals.tf7.enums.GroupType;
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
public class GroupCrudControllerIntTest
    extends AbstractTest {

    @Autowired
    private GroupCrudController groupCrudController;
    private MockMvc restMvc;
    private String baseUrl;
    private Group group;

    @Before
    public void setup()
        throws RegisterBlueprintException, CreateModelException {
        this.restMvc = MockMvcBuilders.standaloneSetup(groupCrudController)
                                      .setMessageConverters(getHttpMessageConverters())
                                      .setControllerAdvice(getExceptionTranslator())
                                      .build();
        this.baseUrl = "/api/v1/groups";

        fakeData();
    }

    @Test
    @Transactional
    public void test_createGroup_shouldBeCreated()
        throws Exception {
        GroupReq req = GroupReq.builder()
                               .name(getFaker().name().name())
                               .type(GroupType.PROJECT)
                               .build();

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(req)))
               .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void test_createGroup_withNameIsNull_shouldBeBadRequest()
        throws Exception {
        GroupReq req = GroupReq.builder()
                               .type(GroupType.PROJECT)
                               .build();

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(req)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_createGroup_withTypeIsNull_shouldBeBadRequest()
        throws Exception {
        GroupReq req = GroupReq.builder()
                               .name(getFaker().name().name())
                               .build();

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(req)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void test_createGroup_withNameAlreadyUsed_shouldBeBadRequest()
        throws Exception {
        GroupReq req = GroupReq.builder()
                               .name(group.getName())
                               .type(GroupType.PROJECT)
                               .build();

        restMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                                              .contentType(APPLICATION_JSON_UTF8)
                                              .content(TestHelper.convertObjectToJsonBytes(req)))
               .andExpect(status().isBadRequest());
    }

    private void fakeData()
        throws RegisterBlueprintException, CreateModelException {
        registerBlueprints(GroupBlueprint.class);

        group = createFakeModel(Group.class);
        group.setType(GroupType.PROJECT);
        getGroupRepository().save(group);
    }
}
