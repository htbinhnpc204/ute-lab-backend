package com.nals.tf7.blueprints;

import com.github.javafaker.Faker;
import com.nals.tf7.domain.Role;
import com.nals.tf7.domain.User;
import com.nals.tf7.enums.Gender;
import com.nals.tf7.helpers.DateHelper;
import com.tobedevoured.modelcitizen.annotation.Blueprint;
import com.tobedevoured.modelcitizen.annotation.Default;
import com.tobedevoured.modelcitizen.callback.FieldCallback;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static com.nals.tf7.config.Constants.DEFAULT_LANG_KEY;
import static com.nals.tf7.enums.Gender.MALE;
import static java.util.Locale.ENGLISH;

@Getter
@Setter
@Blueprint(User.class)
public class UserBlueprint {
    private final Faker faker = new Faker(ENGLISH);

    @Default
    private FieldCallback<String> email = new FieldCallback<>() {
        @Override
        public String get(final Object referenceModel) {
            return faker.internet().emailAddress();
        }
    };

    @Default(force = true)
    private boolean activated = true;

    @Default
    private FieldCallback<String> name = new FieldCallback<>() {
        @Override
        public String get(final Object referenceModel) {
            return faker.name().fullName();
        }
    };

    @Default
    private Instant dob = DateHelper.truncatedNowToDay();

    @Default
    private String phone = "0987654321";

    @Default
    private FieldCallback<String> address = new FieldCallback<>() {
        @Override
        public String get(final Object referenceModel) {
            return faker.address().fullAddress();
        }
    };

    @Default
    private Gender gender = MALE;

    @Default
    private String langKey = DEFAULT_LANG_KEY;

    @Default
    private Role role;

    @Default
    private String password = "$2a$10$MbuTII7CecKnw24JFb.jIutpbYnlFOn6h6ePRIoAxdlfCxCO2tAh2"; // encode ACCOUNT_PASSWORD

    @Default
    private String avatar = String.format("%s.%s", faker.name().name(), "png");
}
