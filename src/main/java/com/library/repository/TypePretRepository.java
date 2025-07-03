package com.library.repository;

import com.library.model.TypePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePretRepository extends JpaRepository<TypePret, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
