package com.library.repository;

import com.library.model.TypeAdherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypeAdherentRepository extends JpaRepository<TypeAdherent, Long> {
    Optional<TypeAdherent> findByNom(String nom);
}
