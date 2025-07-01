package com.library.repository;

import com.library.model.TypePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypePretRepository extends JpaRepository<TypePret, Long> {
    Optional<TypePret> findByNom(String nom);
}
