package com.vimal.user.customEceptions;



public class DuplicateSaveException extends RuntimeException{
    public DuplicateSaveException(String message){
        super(message);
    }
}
