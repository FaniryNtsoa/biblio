package com.library.repository;

import com.library.model.Inscription;
import com.library.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByAdherent(Adherent adherent);
    List<Inscription> findByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Inscription> findByDateExpirationBefore(LocalDate date);
    
    @Query("SELECT i FROM Inscription i WHERE i.adherent.id = :adherentId AND i.dateExpiration >= :currentDate")
    Optional<Inscription> findActiveInscriptionByAdherentId(@Param("adherentId") Long adherentId, @Param("currentDate") LocalDate currentDate);
}
