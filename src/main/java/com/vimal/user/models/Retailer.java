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
public class Retailer extends User {
    @Column(nullable = false)
    private String companyAddress;
    @Column(nullable = false, unique = true)
    private String companyBrand;
    @Column(nullable = false)
    private String companyCity;
    @Column(nullable = false)
    private String companyPincode;
    @Column(nullable = false)
    private String companyState;


}
