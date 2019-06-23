package com.lisak.exception;


public class MotorPolicyServiceFailureException extends RuntimeException {

    public MotorPolicyServiceFailureException(final String message) {
        super(message);
    }

    public MotorPolicyServiceFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
