package com.library.repository;

import com.library.model.Adherent;
import com.library.model.TypeAdherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    List<Adherent> findByNomContainingIgnoreCase(String nom);
    List<Adherent> findByPrenomContainingIgnoreCase(String prenom);
    List<Adherent> findByTypeAdherent(TypeAdherent typeAdherent);
    Optional<Adherent> findByNomAndPrenom(String nom, String prenom);
    List<Adherent> findByDtnBefore(LocalDate date);
    Optional<Adherent> findByEmail(String email);
}
