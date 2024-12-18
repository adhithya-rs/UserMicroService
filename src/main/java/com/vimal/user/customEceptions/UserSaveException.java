package com.vimal.user.customEceptions;



public class UserSaveException extends RuntimeException {
    public UserSaveException(String message) {
        super(message);
    }
}