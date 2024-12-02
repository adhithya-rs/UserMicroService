package com.vimal.user.customEceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTokenException extends RuntimeException{
    public GenerateTokenException(String message) {
        super(message);
    }
}
