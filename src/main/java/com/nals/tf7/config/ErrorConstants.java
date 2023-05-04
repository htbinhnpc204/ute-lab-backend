package com.nals.tf7.config;

/**
 * Application constants.
 */
public final class ErrorConstants {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String GROUP_TYPE = "type";
    public static final String FILE = "file";

    public static final String EMAIL_NOT_NULL = "Email is required";

    public static final String PASSWORD_NOT_NULL = "Password is required";

    public static final String EMAIL_PATTERN_INVALID = "Email is invalid format";

    public static final String EMAIL_PASSWORD_INVALID = "Email or password is invalid";

    public static final String GROUP_NAME_NOT_NULL = "Name is required";

    public static final String GROUP_NAME_USED = "Name is used";

    public static final String GROUP_TYPE_NOT_NULL = "Group type is required";

    public static final String INVALID_UPLOAD_FILE = "Invalid upload file";

    public static final String FILE_NOT_FOUND = "File not found";

    public static final String FILE_NAME_IS_INVALID = "File name is invalid";

    public static final String FILE_EXTENSION_IS_NOT_ALLOW = "File extension is not allow";

    public static final String FILE_DIMENSION_IS_NOT_ALLOW = "File dimension is not allow";

    public static final String FILE_SIZE_IS_NOT_ALLOW = "File size is not allow";

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";

    public static final String USER_NOT_FOUND = "User not found";

    public static final String LAB_NOT_FOUND = "Lab not found";

    public static final String COMPUTER_NOT_FOUND = "Computer not found";

    public static final String ROLE_NOT_FOUND = "Role not found";

    private ErrorConstants() {
    }
}
