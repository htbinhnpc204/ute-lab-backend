package com.nals.tf7.config;

/**
 * Application constants.
 */
public final class ErrorConstants {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String GROUP_TYPE = "type";

    public static final String EMAIL_NOT_NULL = "Email is required";

    public static final String PASSWORD_NOT_NULL = "Password is required";

    public static final String EMAIL_PATTERN_INVALID = "Email is invalid format";

    public static final String EMAIL_PASSWORD_INVALID = "Email or password is invalid";

    public static final String GROUP_NAME_NOT_NULL = "Name is required";

    public static final String GROUP_NAME_USED = "Name is used";

    public static final String GROUP_TYPE_NOT_NULL = "Group type is required";

    public static final String INVALID_UPLOAD_FILE = "Invalid upload file";

    private ErrorConstants() {
    }
}
