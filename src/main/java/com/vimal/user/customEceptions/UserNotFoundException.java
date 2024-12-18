package com.vimal.user.customEceptions;



public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message){
        super(message);

    }
}
