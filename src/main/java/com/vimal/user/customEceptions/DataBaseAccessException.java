package com.vimal.user.customEceptions;

public class DataBaseAccessException extends RuntimeException {
    public DataBaseAccessException(String message) {

        super(message);
    }
}
