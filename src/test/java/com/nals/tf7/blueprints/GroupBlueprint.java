package com.nals.tf7.blueprints;

import com.github.javafaker.Faker;
import com.nals.tf7.domain.Group;
import com.nals.tf7.domain.User;
import com.nals.tf7.enums.GroupType;
import com.tobedevoured.modelcitizen.annotation.Blueprint;
import com.tobedevoured.modelcitizen.annotation.Default;
import com.tobedevoured.modelcitizen.callback.FieldCallback;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static java.util.Locale.ENGLISH;

@Getter
@Setter
@Blueprint(Group.class)
public class GroupBlueprint {
    private final Faker faker = new Faker(ENGLISH);

    @Default
    private FieldCallback<String> name = new FieldCallback<>() {
        @Override
        public String get(final Object referenceModel) {
            return faker.name().name();
        }
    };

    @Default
    private FieldCallback<String> description = new FieldCallback<>() {
        @Override
        public String get(final Object referenceModel) {
            return faker.weather().description();
        }
    };

    @Default
    private GroupType type;

    @Default
    private String avatar = String.format("%s.%s", faker.name().name(), "png");

    @Default
    private List<User> users;
}
