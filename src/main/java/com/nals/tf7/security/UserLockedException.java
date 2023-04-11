package com.nals.tf7.security;

import com.nals.tf7.errors.ErrorCodes;
import com.nals.tf7.errors.ErrorProblem;
import com.nals.tf7.errors.ValidatorException;
import com.nals.tf7.helpers.DateHelper;

import java.time.Instant;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class UserLockedException
    extends ValidatorException {

    private static final long serialVersionUID = -31266416236L;

    public UserLockedException(final Integer limitTryNumber, final Instant unlockTime) {
        super();

        var error = new ErrorProblem();
        error.setMessage("The user was locked, please try again later");
        error.setErrorCode(ErrorCodes.USER_WAS_LOCKED);
        error.setLimitTryNumber(limitTryNumber);
        error.setRemainRetryNumber(0);
        error.setUnlockTime(DateHelper.toMillis(unlockTime));

        addError(error);
    }
}
