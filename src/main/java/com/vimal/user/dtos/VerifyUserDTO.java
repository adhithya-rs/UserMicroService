package com.vimal.user.dtos;

import com.vimal.user.enums.UserType;
import com.vimal.user.models.User;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerifyUserDTO {
    private String email;
    private String phoneNumber;
    private String countryCode;
    private UserType userType;
    private boolean reset;
    private String password;
}
