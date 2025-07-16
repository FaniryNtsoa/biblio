package com.library.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.model.Adherent;
import com.library.model.Exemplaire;
import com.library.model.HistoriquePret;
import com.library.model.Penalite;
import com.library.model.Pret;
import com.library.model.Reservation;
import com.library.model.StatusPret;
import com.library.model.TypePret;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.PenaliteRepository;
import com.library.repository.PretRepository;
import com.library.repository.StatusPretRepository;
import com.library.repository.TypePretRepository;
import com.library.service.PretService;
import com.library.service.ExemplaireAvailabilityService;
import com.library.service.GestionAdherentService;
import com.library.service.DateCalculationService;

@Service
public class PretServiceImpl implements PretService {

    private final PretRepository pretRepository;
    private final HistoriquePretRepository historiquePretRepository;
    private final StatusPretRepository statusPretRepository;
    private final PenaliteRepository penaliteRepository;
    private final TypePretRepository typePretRepository;
    private final ExemplaireAvailabilityService exemplaireAvailabilityService;
    private final GestionAdherentService gestionAdherentService;
    private final DateCalculationService dateCalculationService;

    @Autowired
    public PretServiceImpl(PretRepository pretRepository,
                          HistoriquePretRepository historiquePretRepository,
                          StatusPretRepository statusPretRepository,
                          PenaliteRepository penaliteRepository,
                          TypePretRepository typePretRepository,
                          ExemplaireAvailabilityService exemplaireAvailabilityService,
                          GestionAdherentService gestionAdherentService,
                          DateCalculationService dateCalculationService) {
        this.pretRepository = pretRepository;
        this.historiquePretRepository = historiquePretRepository;
        this.statusPretRepository = statusPretRepository;
        this.penaliteRepository = penaliteRepository;
        this.typePretRepository = typePretRepository;
        this.exemplaireAvailabilityService = exemplaireAvailabilityService;
        this.gestionAdherentService = gestionAdherentService;
        this.dateCalculationService = dateCalculationService;
    }

    @Override
    public List<Pret> getAllPrets() {
        return pretRepository.findAll();
    }

    @Override
    public Optional<Pret> getPretById(Long id) {
        return pretRepository.findById(id);
    }

    @Override
    public Pret savePret(Pret pret) {
        return pretRepository.save(pret);
    }

    @Override
    public void deletePret(Long id) {
        pretRepository.deleteById(id);
    }

    @Override
    public List<Pret> findByAdherent(Adherent adherent) {
        // Implémentation sans méthode personnalisée dans le repository
        List<Pret> prets = pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().equals(adherent))
                .collect(Collectors.toList());
        
        // Précharger les historiques pour éviter les problèmes de lazy loading
        for (Pret pret : prets) {
            pret.setHistoriquePrets(new HashSet<>(historiquePretRepository.findAll().stream()
                .filter(h -> h.getPret().getId().equals(pret.getId()))
                .collect(Collectors.toSet())));
        }
        
        return prets;
    }

    @Override
    public List<Pret> findByTypePret(TypePret typePret) {
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getTypePret().equals(typePret))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pret> findByDatePretBetween(LocalDateTime dateDebut, LocalDateTime dateFin) {
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> !pret.getDatePret().isBefore(dateDebut) && !pret.getDatePret().isAfter(dateFin))
                .collect(Collectors.toList());
    }

    @Override
    public long countByAdherent(Adherent adherent) {
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().equals(adherent))
                .count();
    }

    @Override
    public List<Pret> findPretEnCoursByAdherent(Adherent adherent) {
        StatusPret statusEnCours = findStatusPretByNom("EN_COURS");
        
        if (statusEnCours == null) {
            return Collections.emptyList();
        }
        
        // Récupérer tous les prêts de cet adhérent
        List<Pret> adherentPrets = pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().getId().equals(adherent.getId()))
                .collect(Collectors.toList());
        
        if (adherentPrets.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Récupérer d'abord le dernier statut pour tous les prêts
        Map<Pret, StatusPret> pretStatuses = new HashMap<>();
        for (Pret pret : adherentPrets) {
            pretStatuses.put(pret, getLatestStatus(pret));
        }
        
        // Ensuite filtrer en fonction de ces statuts
        return adherentPrets.stream()
                .filter(pret -> {
                    StatusPret latestStatus = pretStatuses.get(pret);
                    return latestStatus != null && latestStatus.equals(statusEnCours);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Pret> findPretRetournesByAdherent(Adherent adherent) {
        StatusPret statusRendu = findStatusPretByNom("RENDU");
        
        if (statusRendu == null) {
            return Collections.emptyList();
        }
        
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().getId().equals(adherent.getId()))
                .filter(pret -> getLatestStatus(pret).equals(statusRendu))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Pret> findPretEnRetardByAdherent(Adherent adherent) {
        StatusPret statusEnRetard = findStatusPretByNom("EN_RETARD");
        StatusPret statusEnCours = findStatusPretByNom("EN_COURS");
        
        if (statusEnRetard == null || statusEnCours == null) {
            return Collections.emptyList();
        }
        
        LocalDateTime today = LocalDateTime.now();
        
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().getId().equals(adherent.getId()))
                .filter(pret -> {
                    StatusPret latestStatus = getLatestStatus(pret);
                    return latestStatus.equals(statusEnRetard) || 
                           (latestStatus.equals(statusEnCours) && 
                            pret.getDateRetourPrevue() != null && 
                            pret.getDateRetourPrevue().isBefore(today));
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Pret> findPretARendreBientotByAdherent(Adherent adherent, int joursRestants) {
        StatusPret statusEnCours = findStatusPretByNom("EN_COURS");
        
        if (statusEnCours == null) {
            return Collections.emptyList();
        }
        
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime limite = today.plusDays(joursRestants);
        
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().getId().equals(adherent.getId()))
                .filter(pret -> getLatestStatus(pret).equals(statusEnCours))
                .filter(pret -> pret.getDateRetourPrevue() != null && 
                               !pret.getDateRetourPrevue().isBefore(today) && 
                               !pret.getDateRetourPrevue().isAfter(limite))
                .collect(Collectors.toList());
    }
    
    @Override
    public int countPretEnCoursByAdherent(Adherent adherent) {
        return findPretEnCoursByAdherent(adherent).size();
    }
    
    @Override
    public int countPretEnRetardByAdherent(Adherent adherent) {
        return findPretEnRetardByAdherent(adherent).size();
    }
    
    @Override
    public int countPretARendreBientotByAdherent(Adherent adherent, int joursRestants) {
        return findPretARendreBientotByAdherent(adherent, joursRestants).size();
    }
    
    @Override
    public boolean isPretRendu(Pret pret) {
        StatusPret statusRendu = findStatusPretByNom("RENDU");
        
        if (statusRendu == null) {
            return false;
        }
        
        return getLatestStatus(pret).equals(statusRendu);
    }
    
    @Override
    public Penalite savePenalite(Penalite penalite) {
        return penaliteRepository.save(penalite);
    }
    
    @Override
    @Transactional
    public Pret createPretFromReservation(Reservation reservation) {
        // Trouver un exemplaire disponible pour le livre réservé
        Exemplaire exemplaire = exemplaireAvailabilityService.getFirstAvailableExemplaire(reservation.getLivre());
        if (exemplaire == null) {
            throw new RuntimeException("Aucun exemplaire disponible pour ce livre");
        }
        
        // Trouver le type de prêt "a domicile"
        TypePret typePret = typePretRepository.findAll().stream()
                .filter(type -> !type.getSurPlace())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Type de prêt 'a domicile' non trouvé"));
        
        // Créer le prêt
        Pret pret = new Pret();
        pret.setAdherent(reservation.getAdherent());
        pret.setExemplaire(exemplaire);
        pret.setTypePret(typePret);
        pret.setDatePret(reservation.getDateReservation());
        
        // Calculer la date de retour prévue en tenant compte des jours non ouvrés
        int dureePret = gestionAdherentService.getDureePretForAdherent(reservation.getAdherent());
        LocalDateTime dateRetourPrevue = dateCalculationService.addJoursOuvres(reservation.getDateReservation(), dureePret);
        pret.setDateRetourPrevue(dateRetourPrevue);
        
        // Sauvegarder le prêt
        Pret savedPret = savePret(pret);
        
        // Créer l'historique initial avec statut EN_COURS
        StatusPret statusEnCours = statusPretRepository.findAll().stream()
                .filter(status -> "EN_COURS".equals(status.getNom()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Statut EN_COURS non trouvé"));
        
        HistoriquePret historique = new HistoriquePret();
        historique.setPret(savedPret);
        historique.setExemplaire(exemplaire);
        historique.setStatusPret(statusEnCours);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire("Prêt créé à partir d'une réservation");
        
        historiquePretRepository.save(historique);
        
        return savedPret;
    }

    // Méthodes utilitaires privées
    private StatusPret findStatusPretByNom(String nom) {
        return statusPretRepository.findAll().stream()
                .filter(status -> status.getNom().equals(nom))
                .findFirst()
                .orElse(null);
    }
    
    private StatusPret getLatestStatus(Pret pret) {
        // Charger explicitement l'historique si nécessaire
        if (pret.getHistoriquePrets() == null || pret.getHistoriquePrets().isEmpty()) {
            pret.setHistoriquePrets(new HashSet<>(historiquePretRepository.findAll().stream()
                .filter(h -> h.getPret().getId().equals(pret.getId()))
                .collect(Collectors.toSet())));
        }
        
        // Utiliser l'historique préchargé si possible
        if (!pret.getHistoriquePrets().isEmpty()) {
            return pret.getHistoriquePrets().stream()
                .sorted(Comparator.comparing(HistoriquePret::getDateChangement).reversed())
                .map(HistoriquePret::getStatusPret)
                .findFirst()
                .orElse(null);
        } else {
            // Fallback à la méthode originale si l'historique est toujours vide
            return historiquePretRepository.findAll().stream()
                .filter(h -> h.getPret().getId().equals(pret.getId()))
                .sorted(Comparator.comparing(HistoriquePret::getDateChangement).reversed())
                .map(HistoriquePret::getStatusPret)
                .findFirst()
                .orElse(null);
        }
    }
}
