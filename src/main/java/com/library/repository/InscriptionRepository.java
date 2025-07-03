package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.Inscription;


@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
