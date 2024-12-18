package com.vimal.user.customEceptions;



public class GenerateTokenException extends RuntimeException{
    public GenerateTokenException(String message) {
        super(message);
    }
}
