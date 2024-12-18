package com.vimal.user.repositories;

import com.vimal.user.models.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long> {

    Optional<Retailer> findById(Long userId);
}
