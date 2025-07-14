package com.library.service.impl;

import com.library.model.Adherent;
import com.library.model.Penalite;
import com.library.model.Pret;
import com.library.repository.PenaliteRepository;
import com.library.repository.PretRepository;
import com.library.service.PenaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PenaliteServiceImpl implements PenaliteService {

    private final PenaliteRepository penaliteRepository;
    private final PretRepository pretRepository;

    @Autowired
    public PenaliteServiceImpl(PenaliteRepository penaliteRepository, PretRepository pretRepository) {
        this.penaliteRepository = penaliteRepository;
        this.pretRepository = pretRepository;
    }

    @Override
    public List<Penalite> getAllPenalites() {
        return penaliteRepository.findAll();
    }

    @Override
    public Optional<Penalite> getPenaliteById(Long id) {
        return penaliteRepository.findById(id);
    }

    @Override
    public Penalite savePenalite(Penalite penalite) {
        return penaliteRepository.save(penalite);
    }

    @Override
    public void deletePenalite(Long id) {
        penaliteRepository.deleteById(id);
    }

    @Override
    public List<Penalite> findByPret(Pret pret) {
        return penaliteRepository.findAll().stream()
                .filter(penalite -> penalite.getPret().equals(pret))
                .collect(Collectors.toList());
    }

    @Override
    public List<Penalite> findByDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return penaliteRepository.findAll().stream()
                .filter(penalite -> !penalite.getDatePenalite().isBefore(dateDebut) && 
                                   !penalite.getDatePenalite().isAfter(dateFin))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Penalite createPenaliteForPret(Long pretId, String motif) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));
        
        Penalite penalite = new Penalite();
        penalite.setPret(pret);
        penalite.setDescription(motif);
        penalite.setDatePenalite(LocalDateTime.now());
        penalite.setNbJoursRetard(0); // Par défaut
        penalite.setDureeJours(0);    // Par défaut
        penalite.setDateFinPenalite(LocalDateTime.now()); // Par défaut
        penalite.setDateDebutPenalite(LocalDateTime.now()); // Définir la date de début
        penalite.setActive(false);    // Par défaut
        penalite.setDateCreation(LocalDateTime.now());
        
        return penaliteRepository.save(penalite);
    }
    
    @Override
    @Transactional
    public Penalite createPenaliteForRetard(Pret pret, LocalDateTime dateRetour) {
        int joursRetard = calculerJoursRetard(pret, dateRetour);
        
        if (joursRetard <= 0) {
            return null; // Pas de retard, pas de pénalité
        }
        
        Penalite penalite = new Penalite();
        penalite.setPret(pret);
        penalite.setDatePenalite(dateRetour);
        
        // Limiter la description à 50 caractères pour éviter l'erreur de taille
        String descriptionComplete = "Retard de " + joursRetard + " jour(s) pour le retour du livre: " + 
                              pret.getExemplaire().getLivre().getTitre();
        String descriptionTronquee = descriptionComplete.length() > 50 ? 
                                    descriptionComplete.substring(0, 47) + "..." : 
                                    descriptionComplete;
        penalite.setDescription(descriptionTronquee);
        
        penalite.setNbJoursRetard(joursRetard);
        penalite.setDureeJours(joursRetard);
        penalite.setDateCreation(LocalDateTime.now());
        
        // Déterminer la date de début de pénalité
        LocalDateTime dateDebut = dateRetour; // Par défaut, commence à la date de retour
        LocalDateTime dateFinPenaliteExistante = getDateFinPenalite(pret.getAdherent());
        
        if (dateFinPenaliteExistante != null && dateFinPenaliteExistante.isAfter(dateRetour)) {
            // Si une pénalité est déjà en cours, la nouvelle commence après
            dateDebut = dateFinPenaliteExistante;
        }
        
        penalite.setDateDebutPenalite(dateDebut);
        
        // Calculer la date de fin en ajoutant les jours de retard à la date de début
        LocalDateTime dateFin = dateDebut.plusDays(joursRetard);
        penalite.setDateFinPenalite(dateFin);
        penalite.setActive(true);
        
        return penaliteRepository.save(penalite);
    }

    @Override
    public int calculerJoursRetard(Pret pret, LocalDateTime dateRetour) {
        if (pret.getDateRetourPrevue() == null || dateRetour == null) {
            return 0;
        }
        
        // Utiliser la date de retour prévue du prêt qui est déjà mise à jour
        // en cas de prolongement accepté via ProlongementService.acceptProlongement
        // Ainsi, si un prolongement a été accepté, les pénalités sont calculées 
        // par rapport à la nouvelle date de retour prévue
        long joursRetard = ChronoUnit.DAYS.between(pret.getDateRetourPrevue(), dateRetour);
        return joursRetard > 0 ? (int) joursRetard : 0;
    }

    @Override
    public boolean hasActivePenalites(Adherent adherent) {
        updatePenaliteStatuses(); // Mettre à jour les statuts des pénalités d'abord
        
        LocalDateTime today = LocalDateTime.now();
        return penaliteRepository.findAll().stream()
                .filter(p -> p.getPret().getAdherent().getId().equals(adherent.getId()))
                .filter(p -> p.getActive())
                .filter(p -> !p.getDateFinPenalite().isBefore(today))
                .findAny()
                .isPresent();
    }

    @Override
    public LocalDateTime getDateFinPenalite(Adherent adherent) {
        updatePenaliteStatuses(); // Mettre à jour les statuts des pénalités d'abord
        
        LocalDateTime today = LocalDateTime.now();
        return penaliteRepository.findAll().stream()
                .filter(p -> p.getPret().getAdherent().getId().equals(adherent.getId()))
                .filter(p -> p.getActive())
                .filter(p -> !p.getDateFinPenalite().isBefore(today))
                .map(Penalite::getDateFinPenalite)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public List<Penalite> getRecentPenalites(int limit) {
        return penaliteRepository.findAll().stream()
                .sorted(Comparator.comparing(Penalite::getDatePenalite).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Penalite> findActiveByAdherent(Adherent adherent) {
        updatePenaliteStatuses(); // Mettre à jour les statuts des pénalités d'abord
        
        LocalDateTime today = LocalDateTime.now();
        return penaliteRepository.findAll().stream()
                .filter(p -> p.getPret().getAdherent().getId().equals(adherent.getId()))
                .filter(p -> p.getActive())
                .filter(p -> !p.getDateFinPenalite().isBefore(today))
                .sorted(Comparator.comparing(Penalite::getDateFinPenalite))
                .collect(Collectors.toList());
    }

    @Override
    public List<Penalite> findAllByAdherent(Adherent adherent) {
        return penaliteRepository.findAll().stream()
                .filter(p -> p.getPret().getAdherent().getId().equals(adherent.getId()))
                .sorted(Comparator.comparing(Penalite::getDatePenalite).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updatePenaliteStatuses() {
        LocalDateTime today = LocalDateTime.now();
        List<Penalite> penalites = penaliteRepository.findAll().stream()
                .filter(p -> p.getActive())
                .filter(p -> p.getDateFinPenalite().isBefore(today))
                .collect(Collectors.toList());
        
        for (Penalite penalite : penalites) {
            penalite.setActive(false);
            penaliteRepository.save(penalite);
        }
    }

    @Override
    public boolean isPenalityActiveOnDate(Adherent adherent, LocalDateTime date) {
        updatePenaliteStatuses(); // Mettre à jour les statuts des pénalités d'abord
        
        return penaliteRepository.findAll().stream()
                .filter(p -> p.getPret().getAdherent().getId().equals(adherent.getId()))
                .filter(p -> p.getActive())
                .anyMatch(p -> !date.isBefore(p.getDateDebutPenalite()) && !date.isAfter(p.getDateFinPenalite()));
    }

    @Override
    public boolean isDateInPenalitePeriod(Adherent adherent, LocalDateTime date) {
        // Récupérer toutes les pénalités actives
        List<Penalite> penalitesActives = findActiveByAdherent(adherent);
        
        // Vérifier si la date tombe dans une période de pénalité
        for (Penalite penalite : penalitesActives) {
            // Si la date est entre le début et la fin de la pénalité
            if ((penalite.getDateDebutPenalite().isEqual(date) || penalite.getDateDebutPenalite().isBefore(date)) &&
                (penalite.getDateFinPenalite().isEqual(date) || penalite.getDateFinPenalite().isAfter(date))) {
                return true;
            }
        }
        
        return false;
    }
}