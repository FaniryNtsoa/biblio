package com.library.service;

import com.library.model.GestionAdherent;
import com.library.model.TypeAdherent;
import com.library.model.Adherent;
import java.util.List;
import java.util.Optional;

public interface GestionAdherentService {
    List<GestionAdherent> getAllGestionAdherents();
    Optional<GestionAdherent> getGestionAdherentById(Long id);
    GestionAdherent saveGestionAdherent(GestionAdherent gestionAdherent);
    void deleteGestionAdherent(Long id);
    
    // Méthodes spécifiques pour les règles de gestion
    Optional<GestionAdherent> findByTypeAdherent(TypeAdherent typeAdherent);
    int getDureePretForAdherent(Adherent adherent);
    int getNombrePretMaxForAdherent(Adherent adherent);
    boolean canAdherentBorrow(Adherent adherent, int currentPretCount);
}
