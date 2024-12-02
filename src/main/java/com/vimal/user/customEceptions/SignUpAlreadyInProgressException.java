package com.vimal.user.customEceptions;

public class SignUpAlreadyInProgressException extends RuntimeException {
    public SignUpAlreadyInProgressException(String message) {
        super(message);
    }
}
