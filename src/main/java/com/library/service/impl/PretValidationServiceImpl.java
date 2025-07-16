package com.library.service.impl;

import com.library.model.*;
import com.library.repository.GestionAdherentRepository;
import com.library.repository.PretRepository;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.StatusPretRepository;
import com.library.service.PretValidationService;
import com.library.service.GestionAdherentService;
import com.library.service.PenaliteService;
import com.library.service.DateCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PretValidationServiceImpl implements PretValidationService {

    private final GestionAdherentRepository gestionAdherentRepository;
    private final PretRepository pretRepository;
    private final HistoriquePretRepository historiquePretRepository;
    private final StatusPretRepository statusPretRepository;
    private final GestionAdherentService gestionAdherentService;
    private final PenaliteService penaliteService;
    private final DateCalculationService dateCalculationService;
    
    private String validationMessage;

    @Autowired
    public PretValidationServiceImpl(GestionAdherentRepository gestionAdherentRepository,
                                   PretRepository pretRepository,
                                   HistoriquePretRepository historiquePretRepository,
                                   StatusPretRepository statusPretRepository,
                                   GestionAdherentService gestionAdherentService,
                                   PenaliteService penaliteService,
                                   DateCalculationService dateCalculationService) {
        this.gestionAdherentRepository = gestionAdherentRepository;
        this.pretRepository = pretRepository;
        this.historiquePretRepository = historiquePretRepository;
        this.statusPretRepository = statusPretRepository;
        this.gestionAdherentService = gestionAdherentService;
        this.penaliteService = penaliteService;
        this.dateCalculationService = dateCalculationService;
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

    @Override
    public boolean validateNoPenalites(Adherent adherent) {
        if (penaliteService.hasActivePenalites(adherent)) {
            LocalDateTime dateFinPenalite = penaliteService.getDateFinPenalite(adherent);
            validationMessage = "Vous avez une pénalité en cours jusqu'au " + 
                               dateFinPenalite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                               ". Veuillez attendre la fin de cette période pour emprunter à nouveau.";
            // Retourne false pour bloquer l'emprunt
            return false;
        }
        return true;
    }

    @Override
    public boolean validateNoPenalitesOnDate(Adherent adherent, LocalDateTime datePret) {
        if (penaliteService.isPenalityActiveOnDate(adherent, datePret)) {
            LocalDateTime dateFinPenalite = penaliteService.getDateFinPenalite(adherent);
            validationMessage = "Vous avez une pénalité active à la date demandée (" + 
                               datePret.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                               "). Veuillez choisir une date après le " + 
                               dateFinPenalite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + 
                               " ou contacter un administrateur.";
            return false;
        }
        return true;
    }
    
    // Surcharge pour accepter LocalDate et le convertir en LocalDateTime
    @Override
    public boolean validateNoPenalitesOnDate(Adherent adherent, LocalDate datePret) {
        return validateNoPenalitesOnDate(adherent, datePret.atStartOfDay());
    }
    
    @Override
    public boolean validateJourOuvre(LocalDateTime date) {
        if (!dateCalculationService.isJourOuvre(date)) {
            validationMessage = "La date sélectionnée (" + 
                               date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                               ") est un jour férié ou un dimanche. Veuillez choisir un jour ouvré.";
            return false;
        }
        return true;
    }
    
    @Override
    public boolean validateJourOuvre(LocalDate date) {
        return validateJourOuvre(date.atStartOfDay());
    }
}

