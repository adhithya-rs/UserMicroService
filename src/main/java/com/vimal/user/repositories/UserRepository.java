package com.vimal.user.repositories;

import com.vimal.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndCountryCode(String phoneNumber, String countryCode);

    User findByEmail(String email);

    User findByPhoneNumberAndCountryCode(String phoneNumber, String countryCode);
}
