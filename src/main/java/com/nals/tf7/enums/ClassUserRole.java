package com.nals.tf7.enums;

import com.nals.tf7.errors.ObjectNotFoundException;

import java.util.Arrays;

import static com.nals.tf7.errors.ErrorCodes.GROUP_USER_ROLE_NOT_FOUND;

public enum ClassUserRole {
    GIANG_VIEN, LOP_TRUONG, BI_THU, THANH_VIEN;

    public static ClassUserRole get(final String value) {
        return Arrays.stream(values())
                     .filter(role -> role.name().equalsIgnoreCase(value))
                     .findFirst()
                     .orElseThrow(() -> new ObjectNotFoundException("groupUserRole", GROUP_USER_ROLE_NOT_FOUND));
    }
}
