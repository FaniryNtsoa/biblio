package com.library.repository;

import com.library.model.Penalite;
import com.library.model.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Long> {
    List<Penalite> findByPret(Pret pret);
    List<Penalite> findByDatePenaliteBetween(LocalDate dateDebut, LocalDate dateFin);
}
