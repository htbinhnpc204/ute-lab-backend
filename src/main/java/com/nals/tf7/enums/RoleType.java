package com.nals.tf7.enums;

import com.nals.tf7.errors.ObjectNotFoundException;

import java.util.Arrays;

import static com.nals.tf7.errors.ErrorCodes.ROLE_TYPE_NOT_FOUND;

public enum RoleType {
    ROLE_GIAO_VIEN, ROLE_QUAN_TRI, ROLE_DAO_TAO, ROLE_SINH_VIEN;

    public static RoleType get(final String value) {
        return Arrays.stream(values())
                     .filter(roleType -> roleType.name().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new ObjectNotFoundException("role_type", ROLE_TYPE_NOT_FOUND));
    }
}
