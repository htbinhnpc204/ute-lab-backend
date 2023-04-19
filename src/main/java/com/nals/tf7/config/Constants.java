package com.nals.tf7.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String SYSTEM = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String USER_ID_LOG_NAME = "userId";
    public static final String USER_ID_HEADER_NAME = "X-UserId";
    public static final String REQUEST_ID_HEADER_NAME = "X-RequestId";
    public static final String DEFAULT_LANG_KEY = "ja";
    public static final int REQUEST_ID_LENGTH = 6;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*"
        + "@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    public static final String PHONE_PATTERN = "^\\d+$";
    public static final String POSTAL_CODE_PATTERN = "^\\d{3}-\\d{4}$";

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String COMMA = ",";

    private Constants() {
    }
}
