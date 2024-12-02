package com.vimal.user.dtos;

import com.vimal.user.enums.UserType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String companyBrand;
    private String companyCity;
    private String companyAddress;
    private String companyPincode;
    private String companyState;
    private UserType userType;
    private String countryCode;

}
