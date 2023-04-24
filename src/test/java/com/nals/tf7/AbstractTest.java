package com.nals.tf7;

import com.github.javafaker.Faker;
import com.nals.tf7.bloc.v1.UserCrudBloc;
import com.nals.tf7.config.ApplicationProperties;
import com.nals.tf7.domain.Permission;
import com.nals.tf7.domain.Role;
import com.nals.tf7.domain.RolePermission;
import com.nals.tf7.domain.User;
import com.nals.tf7.errors.ExceptionTranslator;
import com.nals.tf7.repository.GroupRepository;
import com.nals.tf7.repository.PermissionRepository;
import com.nals.tf7.repository.RolePermissionRepository;
import com.nals.tf7.repository.RoleRepository;
import com.nals.tf7.repository.UserRepository;
import com.nals.tf7.security.DomainUserDetails;
import com.nals.tf7.service.v1.FileService;
import com.tobedevoured.modelcitizen.CreateModelException;
import com.tobedevoured.modelcitizen.ModelFactory;
import com.tobedevoured.modelcitizen.RegisterBlueprintException;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Component
public class AbstractTest {

    public static final String ACCOUNT_PASSWORD = "tf7123#@!";
    public static final Long INVALID_ID = -1L;
    public static final Long CURRENT_USER_ID = 99L;
    public static final String CURRENT_USER_EMAIL = "user@nal.vn";
    private static final String DEFAULT_TIME_ZONE = "Asia/Ho_Chi_Minh";
    private static final String FILE_SERVICE = "fileService";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpMessageConverter<?>[] httpMessageConverters;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Mock
    private ApplicationProperties mockApplicationProperties;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserCrudBloc userCrudBloc;

    private ModelFactory modelFactory;
    private Faker faker;
    private ZoneId zoneId;

    @Before
    public void before() {
        this.modelFactory = new ModelFactory();
        this.faker = new Faker();

        Mockito.when(mockApplicationProperties.getTimezone())
               .thenReturn(DEFAULT_TIME_ZONE);

        zoneId = ZoneId.of(mockApplicationProperties.getTimezone());
    }

    protected void createUserAndRoles(final String role)
        throws CreateModelException {
        User user = createFakeModel(User.class);
        userRepository.save(user);

        roleRepository.findByName(role)
                      .orElseGet(() -> roleRepository.save(Role.builder().name(role).build()));
    }

    protected void mockFileService(final Object bloc)
        throws IOException {
        ReflectionTestUtils.setField(bloc, FILE_SERVICE, fileService);
    }

    protected void createRoleAndPermissions(final User user,
                                            final String role,
                                            final String... permissions) {

        Long roleId = roleRepository.findByName(role)
                                    .orElseGet(() -> roleRepository.save(Role.builder().name(role).build()))
                                    .getId();

        for (String permission : permissions) {
            Long permissionId = permissionRepository.findByName(permission)
                                                    .orElseGet(() -> permissionRepository.save(Permission.builder()
                                                                                                         .name(role)
                                                                                                         .build()))
                                                    .getId();
            rolePermissionRepository.save(RolePermission.builder()
                                                        .roleId(roleId)
                                                        .permissionId(permissionId)
                                                        .build());
        }
    }

    protected void mockAuthentication(final User user,
                                      final String role,
                                      final String... permissions) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<GrantedAuthority> roles = Set.of(new SimpleGrantedAuthority(role));

        Long userId = user.getId();
        Long roleId = roleRepository.findByName(role)
                                    .orElseGet(() -> roleRepository.save(Role.builder().name(role).build()))
                                    .getId();

        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
            Long permissionId = permissionRepository.findByName(permission)
                                                    .orElseGet(() -> permissionRepository.save(Permission.builder()
                                                                                                         .name(role)
                                                                                                         .build()))
                                                    .getId();
            rolePermissionRepository.save(RolePermission.builder()
                                                        .roleId(roleId)
                                                        .permissionId(permissionId)
                                                        .build());
        }

        DomainUserDetails principal = new DomainUserDetails(userId,
                                                            user.getEmail(),
                                                            user.getPassword(),
                                                            roles, authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void registerBlueprints(final Class<?>... classes)
        throws RegisterBlueprintException {
        modelFactory.setRegisterBlueprints(Arrays.asList(classes));
    }

    public <T> T createFakeModel(final Class<T> clazz)
        throws CreateModelException {
        return modelFactory.createModel(clazz, true);
    }

    public JsonPathResultMatchers jsonPath(final String expression) {
        return MockMvcResultMatchers.jsonPath(expression);
    }

    public ResultMatcher matchJsonPath(final String expression, final Object expectedValue) {
        return jsonPath(expression).value(expectedValue);
    }
}
