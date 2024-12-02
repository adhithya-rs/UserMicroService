package com.vimal.user.repositories;

import com.vimal.user.models.RegisterJWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RegisterJWTRepository extends JpaRepository<RegisterJWT,Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    void deleteByPhoneNumber(String phoneNumber);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<RegisterJWT> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndCountryCode(String phoneNumber, String countryCode);

    void deleteByPhoneNumberAndCountryCode(String phoneNumber, String countryCode);

    Optional<RegisterJWT> findByEmail(String email);
}
