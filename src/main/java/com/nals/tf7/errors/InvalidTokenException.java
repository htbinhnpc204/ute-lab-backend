package com.nals.tf7.errors;

public class InvalidTokenException
    extends ValidatorException {

    public InvalidTokenException() {
        super("Invalid refresh token", "token", ErrorCodes.INVALID_REFRESH_TOKEN);
    }
}
