package com.nals.tf7.errors;

public final class ErrorCodes {
    private ErrorCodes() {
    }

    // Common
    public static final String HANDLE_FILE_FAILED = "RW-0100";
    public static final String VALIDATOR = "RW-0101";
    public static final String DATA_CONSTRAINT = "RW-0102";
    public static final String DUPLICATE_DATA = "RW-0103";
    public static final String SECURITY = "RW-0103=4";

    // Validator exception error code (Default: RW-0300)
    public static final String INVALID_KEY = "RW-0300";
    public static final String INVALID_USERNAME_OR_PASSWORD = "RW-0301";
    public static final String USER_WAS_LOCKED = "RW-0302";
    public static final String INVALID_REFRESH_TOKEN = "RW-0304";
    public static final String INVALID_RESET_KEY = "RW-0305";
    public static final String INVALID_USER_SITE = "RW-0306";

    // Validator exception error (Default: RW-0400)
    public static final String BAD_REQUEST = "RW-0400";
    public static final String UNAUTHORIZED = "RW-0401";
    public static final String FORBIDDEN = "RW-0402";
    public static final String NOT_FOUND = "RW-0403";
    public static final String METHOD_NOT_ALLOWED = "RW-0404";
    public static final String NOT_ACCEPTABLE = "RW-0405";
    public static final String UNSUPPORTED_MEDIA_TYPE = "RW-0406";

    // Internal server error (Default: RW-0500)
    public static final String INTERNAL_SERVER = "RW-0500";
    public static final String NOT_IMPLEMENTED = "RW-0501";

    // Object not found exception error (Default: RW-0600)
    public static final String OBJECT_NOT_FOUND = "RW-0600";
    public static final String ROLE_NOT_FOUND = "RW-0601";
    public static final String USER_NOT_FOUND = "RW-0602";
    public static final String GENDER_NOT_FOUND = "RW-0603";
    public static final String PERMISSION_TYPE_NOT_FOUND = "RW-0604";
    public static final String ROLE_TYPE_NOT_FOUND = "RW-0605";
    public static final String AUTH_PROVIDER_NOT_FOUND = "RW-0606";
    public static final String MEDIA_NOT_FOUND = "RW-0607";
    public static final String STATUS_NOT_FOUND = "RW-0608";
}
