package com.nals.tf7.errors;

import static com.nals.tf7.config.ErrorConstants.EMAIL_PASSWORD_INVALID;
import static com.nals.tf7.errors.ErrorCodes.INVALID_EMAIL_OR_PASSWORD;

public class InvalidCredentialException
    extends ValidatorException {

    public InvalidCredentialException() {
        super();

        var error = new ErrorProblem();
        error.setMessage(EMAIL_PASSWORD_INVALID);
        error.setErrorCode(INVALID_EMAIL_OR_PASSWORD);

        addError(error);
    }

    public InvalidCredentialException(final String field, final String message, final String errorCode) {
        super();

        var error = new ErrorProblem();
        error.setField(field);
        error.setMessage(message);
        error.setErrorCode(errorCode);

        addError(error);
    }
}
