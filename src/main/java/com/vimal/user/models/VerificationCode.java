package com.vimal.user.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    @Id
    private long id;
    @Column(nullable = false)
    private String email;
    @Column
    private String phoneNumberVerificationCode;
    @Column
    private String emailVerificationCode;
    @Column
    private String verificationCode;



    public VerificationCode(String email, String phoneNumberCode, String emailCode) {
        this.email = email;
        this.phoneNumberVerificationCode = phoneNumberCode;
        this.emailVerificationCode = emailCode;
    }

    public VerificationCode(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
