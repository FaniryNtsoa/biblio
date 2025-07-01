package com.library.repository;

import com.library.model.EtatExemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EtatExemplaireRepository extends JpaRepository<EtatExemplaire, Long> {
    Optional<EtatExemplaire> findByNom(String nom);
}
