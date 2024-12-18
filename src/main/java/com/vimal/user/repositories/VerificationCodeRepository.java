package com.vimal.user.repositories;

import com.vimal.user.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {


    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    VerificationCode findByEmail(String email);
}
