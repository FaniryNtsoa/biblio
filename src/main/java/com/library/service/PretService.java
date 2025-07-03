package com.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.library.model.Adherent;
import com.library.model.Pret;
import com.library.model.TypePret;

public interface PretService {
    List<Pret> getAllPrets();
    Optional<Pret> getPretById(Long id);
    Pret savePret(Pret pret);
    void deletePret(Long id);
    
    // Méthodes de recherche
    List<Pret> findByAdherent(Adherent adherent);
    List<Pret> findByTypePret(TypePret typePret);
    List<Pret> findByDatePretBetween(LocalDate dateDebut, LocalDate dateFin);
    long countByAdherent(Adherent adherent);
    
}
