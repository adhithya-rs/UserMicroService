package com.vimal.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vimal.user.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"phoneNumber", "countryCode"})
        },
        indexes = {
                @Index(name = "idx_phone_country", columnList = "phoneNumber, countryCode"),
                @Index(name = "idx_email", columnList = "email")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false, length = 5)
    private String countryCode;
    @Column(nullable = false)
    private UserStatus userStatus;
    @Column(nullable = false)
    private UserType userType;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date lastModifiedAt;
    @JsonIgnore
    @Column(nullable = false)
    private boolean isDeleted;


    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.lastModifiedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        this.lastModifiedAt = new Date();
    }

}
