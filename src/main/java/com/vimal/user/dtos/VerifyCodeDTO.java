package com.vimal.user.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeDTO {
    private String phoneNumberVerificationCode;
    private String emailVerificationCode;
    private String verificationCode;
    private String email;
}
