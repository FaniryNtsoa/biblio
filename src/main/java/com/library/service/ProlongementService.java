package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.model.Adherent;
import com.library.model.Pret;
import com.library.model.Prolongement;
import com.library.model.StatusProlongement;

public interface ProlongementService {
    List<Prolongement> getAllProlongements();
    Optional<Prolongement> getProlongementById(Long id);
    Prolongement saveProlongement(Prolongement prolongement);
    void deleteProlongement(Long id);
    
    // Méthodes pour la gestion des prolongements
    Prolongement createProlongementForPret(Long pretId, int nbJours);
    Prolongement updateStatusProlongement(Long prolongementId, StatusProlongement statusProlongement);
    
    // Nouvelles méthodes
    boolean hasPretActiveProlongement(Pret pret);
    boolean hasPretCompletedProlongement(Pret pret);
    Optional<Prolongement> findActiveProlongementByPret(Pret pret);
    List<Prolongement> findPendingProlongements();
    Prolongement createProlongementRequest(Pret pret);
    boolean acceptProlongement(Long prolongementId);
    boolean rejectProlongement(Long prolongementId);
    boolean cancelProlongement(Long prolongementId);
    String getLatestStatusName(Prolongement prolongement);
    boolean isProlongementEnAttente(Prolongement prolongement);
    boolean isProlongementAccepte(Prolongement prolongement);
}
