package com.library.service.impl;

import com.library.model.*;
import com.library.repository.GestionAdherentRepository;
import com.library.repository.PretRepository;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.StatusPretRepository;
import com.library.service.PretValidationService;
import com.library.service.GestionAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class PretValidationServiceImpl implements PretValidationService {

    private final GestionAdherentRepository gestionAdherentRepository;
    private final PretRepository pretRepository;
    private final HistoriquePretRepository historiquePretRepository;
    private final StatusPretRepository statusPretRepository;
    private final GestionAdherentService gestionAdherentService;
    
    private String validationMessage;

    @Autowired
    public PretValidationServiceImpl(GestionAdherentRepository gestionAdherentRepository,
                                   PretRepository pretRepository,
                                   HistoriquePretRepository historiquePretRepository,
                                   StatusPretRepository statusPretRepository,
                                   GestionAdherentService gestionAdherentService) {
        this.gestionAdherentRepository = gestionAdherentRepository;
        this.pretRepository = pretRepository;
        this.historiquePretRepository = historiquePretRepository;
        this.statusPretRepository = statusPretRepository;
        this.gestionAdherentService = gestionAdherentService;
    }

    @Override
    public boolean validateAge(Adherent adherent, Exemplaire exemplaire) {
        int ageAdherent = Period.between(adherent.getDtn(), LocalDate.now()).getYears();
        int ageMinLivre = exemplaire.getLivre().getAgeMin();
        
        if (ageAdherent < ageMinLivre) {
            validationMessage = String.format("Vous devez avoir au moins %d ans pour emprunter ce livre. Votre âge: %d ans.", 
                                             ageMinLivre, ageAdherent);
            return false;
        }
        return true;
    }

    @Override
    public boolean validatePretLimit(Adherent adherent, TypePret typePret) {
        // Si c'est un prêt sur place, pas de limite
        if (typePret.getSurPlace()) {
            return true;
        }
        
        // Utiliser le service GestionAdherent pour obtenir les règles
        int maxPrets = gestionAdherentService.getNombrePretMaxForAdherent(adherent);
        
        // Compter les prêts en cours (non rendus)
        StatusPret statusEnCours = statusPretRepository.findAll().stream()
                .filter(s -> s.getNom().equals("EN_COURS"))
                .findFirst()
                .orElse(null);
        
        if (statusEnCours == null) {
            validationMessage = "Statut 'EN_COURS' non trouvé.";
            return false;
        }
        
        long pretsEnCours = historiquePretRepository.findAll().stream()
                .filter(h -> h.getPret().getAdherent().getId().equals(adherent.getId()))
                .filter(h -> h.getStatusPret().getId().equals(statusEnCours.getId()))
                .filter(h -> historiquePretRepository.findAll().stream()
                    .noneMatch(h2 -> h2.getPret().getId().equals(h.getPret().getId()) && 
                               h2.getDateChangement().isAfter(h.getDateChangement()) &&
                               (h2.getStatusPret().getNom().equals("RENDU") || h2.getStatusPret().getNom().equals("EN_RETARD"))))
                .count();
        
        if (pretsEnCours >= maxPrets) {
            validationMessage = String.format("Vous avez atteint le nombre maximum de prêts autorisé (%d). Prêts en cours: %d", 
                                             maxPrets, pretsEnCours);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean validateExemplaireAvailability(Exemplaire exemplaire) {
        // Vérifier que l'exemplaire n'est pas déjà emprunté
        StatusPret statusEnCours = statusPretRepository.findAll().stream()
                .filter(s -> s.getNom().equals("EN_COURS"))
                .findFirst()
                .orElse(null);
        
        if (statusEnCours == null) {
            validationMessage = "Statut 'EN_COURS' non trouvé.";
            return false;
        }
        
        // Vérifier si cet exemplaire est actuellement emprunté
        boolean isEmprunte = historiquePretRepository.findAll().stream()
                .filter(h -> h.getExemplaire().getId().equals(exemplaire.getId()))
                .filter(h -> h.getStatusPret().getId().equals(statusEnCours.getId()))
                .anyMatch(h -> historiquePretRepository.findAll().stream()
                    .noneMatch(h2 -> h2.getPret().getId().equals(h.getPret().getId()) && 
                               h2.getDateChangement().isAfter(h.getDateChangement()) &&
                               (h2.getStatusPret().getNom().equals("RENDU") || h2.getStatusPret().getNom().equals("EN_RETARD"))));
        
        if (isEmprunte) {
            validationMessage = "Cet exemplaire est actuellement emprunté.";
            return false;
        }
        
        return true;
    }

    @Override
    public int getMaxPretDuration(Adherent adherent) {
        return gestionAdherentService.getDureePretForAdherent(adherent);
    }

    @Override
    public String getValidationMessage() {
        return validationMessage;
    }
}

