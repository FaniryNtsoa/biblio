package com.library.repository;

import com.library.model.TypeAdherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TypeAdherentRepository extends JpaRepository<TypeAdherent, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
