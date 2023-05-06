package com.nals.tf7.enums;

import com.nals.tf7.errors.ObjectNotFoundException;

import java.util.Arrays;

import static com.nals.tf7.errors.ErrorCodes.GROUP_USER_ROLE_NOT_FOUND;

public enum ClassUserRole {
    PM_PO("PM/PO"), APO("APO"), SM("SM"), MEMBER("MEMBER"), NON_TECH("NON_TECH");

    ClassUserRole(final String value) {
    }

    public static ClassUserRole get(final String value) {
        return Arrays.stream(values())
                     .filter(role -> role.name().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new ObjectNotFoundException("groupUserRole", GROUP_USER_ROLE_NOT_FOUND));
    }
}
