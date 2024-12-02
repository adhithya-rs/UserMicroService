package com.vimal.user.customEceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateSaveException extends RuntimeException{
    public DuplicateSaveException(String message){
        super(message);
    }
}
