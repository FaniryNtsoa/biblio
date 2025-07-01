package com.library.repository;

import com.library.model.StatusPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusPretRepository extends JpaRepository<StatusPret, Long> {
    Optional<StatusPret> findByNom(String nom);
}
