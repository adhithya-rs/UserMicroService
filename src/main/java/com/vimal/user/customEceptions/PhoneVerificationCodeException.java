package com.vimal.user.customEceptions;

public class PhoneVerificationCodeException extends RuntimeException {
    public PhoneVerificationCodeException(String message) {
        super(message);
    }
}
