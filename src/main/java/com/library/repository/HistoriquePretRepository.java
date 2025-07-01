package com.library.repository;

import com.library.model.HistoriquePret;
import com.library.model.Pret;
import com.library.model.StatusPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriquePretRepository extends JpaRepository<HistoriquePret, Long> {
    List<HistoriquePret> findByPret(Pret pret);
    List<HistoriquePret> findByStatusPret(StatusPret statusPret);
    List<HistoriquePret> findByDateRetourBetween(LocalDate dateDebut, LocalDate dateFin);
}
