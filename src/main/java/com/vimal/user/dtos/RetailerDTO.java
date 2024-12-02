package com.vimal.user.dtos;

import com.vimal.user.enums.UserStatus;
import com.vimal.user.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RetailerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String countryCode;
    private UserStatus userStatus;
    private String companyAddress;
    private String companyBrand;
    private String companyCity;
    private String companyState;
    private String companyPincode;

    public RetailerDTO(String firstName, String lastName, String email, String phoneNumber,
                       String countryCode, UserStatus userStatus,
                       String companyAddress, String companyBrand, String companyCity,
                       String companyState, String companyPincode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.userStatus = userStatus;
        this.companyAddress = companyAddress;
        this.companyBrand = companyBrand;
        this.companyCity = companyCity;
        this.companyState = companyState;
        this.companyPincode = companyPincode;
    }
}
