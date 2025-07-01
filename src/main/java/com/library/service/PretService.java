package com.library.service;

import com.library.model.Pret;
import com.library.model.Adherent;
import com.library.model.TypePret;
import com.library.model.StatusPret;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PretService {
    List<Pret> getAllPrets();
    Optional<Pret> getPretById(Long id);
    Pret savePret(Pret pret);
    void deletePret(Long id);
    List<Pret> findByAdherent(Adherent adherent);
    List<Pret> findByTypePret(TypePret typePret);
    List<Pret> findByDatePretBetween(LocalDate dateDebut, LocalDate dateFin);
    long countByAdherent(Adherent adherent);
    Pret updateStatusPret(Long pretId, StatusPret statusPret, LocalDate dateRetour);
    Pret prolongerPret(Long pretId, int nbJours);
}
