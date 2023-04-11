package com.nals.tf7.errors;

import static com.nals.tf7.errors.ErrorCodes.HANDLE_FILE_FAILED;

public class FileException
    extends ValidatorException {

    public FileException(final String message) {
        super(message, "file", HANDLE_FILE_FAILED);
    }

    public FileException(final String message, final Throwable throwable) {
        super(message, throwable, "file", HANDLE_FILE_FAILED);
    }
}
