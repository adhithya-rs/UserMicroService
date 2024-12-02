package com.vimal.user.customEceptions;

public class EmailVerificationCodeException extends RuntimeException {
    public EmailVerificationCodeException(String message) {
        super(message);
    }
}
