package com.library.repository;

import com.library.model.Pret;
import com.library.model.Adherent;
import com.library.model.TypePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PretRepository extends JpaRepository<Pret, Long> {
    List<Pret> findByAdherent(Adherent adherent);
    List<Pret> findByTypePret(TypePret typePret);
    List<Pret> findByDatePretBetween(LocalDate dateDebut, LocalDate dateFin);
    long countByAdherent(Adherent adherent);
}
