package com.nals.tf7.enums;

import com.nals.tf7.errors.ObjectNotFoundException;

import java.util.Arrays;

import static com.nals.tf7.errors.ErrorCodes.GENDER_NOT_FOUND;

public enum Gender {
    MALE, FEMALE, OTHER;

    public static Gender get(final String value) {
        return Arrays.stream(values()).filter(gender -> gender.name().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new ObjectNotFoundException("gender", GENDER_NOT_FOUND));
    }
}
