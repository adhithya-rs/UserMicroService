package com.vimal.user.repositories;

import com.vimal.user.models.RegisterJWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RegisterJWTRepository extends JpaRepository<RegisterJWT,Long> {


    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<RegisterJWT> findByEmail(String email);
}
