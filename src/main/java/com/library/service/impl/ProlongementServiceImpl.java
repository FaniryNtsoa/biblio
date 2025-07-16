package com.library.service.impl;

import com.library.model.Prolongement;
import com.library.model.HistoriqueProlongement;
import com.library.model.Pret;
import com.library.model.StatusProlongement;
import com.library.repository.ProlongementRepository;
import com.library.repository.HistoriqueProlongementRepository;
import com.library.repository.PretRepository;
import com.library.repository.StatusProlongementRepository;
import com.library.service.ProlongementService;
import com.library.service.GestionAdherentService;
import com.library.service.PretService;
import com.library.service.DateCalculationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProlongementServiceImpl implements ProlongementService {

    private final ProlongementRepository prolongementRepository;
    private final HistoriqueProlongementRepository historiqueProlongementRepository;
    private final PretRepository pretRepository;
    private final StatusProlongementRepository statusProlongementRepository;
    private final GestionAdherentService gestionAdherentService;
    private final PretService pretService;
    private final DateCalculationService dateCalculationService;

    @Autowired
    public ProlongementServiceImpl(ProlongementRepository prolongementRepository,
                                  HistoriqueProlongementRepository historiqueProlongementRepository,
                                  PretRepository pretRepository,
                                  StatusProlongementRepository statusProlongementRepository,
                                  GestionAdherentService gestionAdherentService,
                                  PretService pretService,
                                  DateCalculationService dateCalculationService) {
        this.prolongementRepository = prolongementRepository;
        this.historiqueProlongementRepository = historiqueProlongementRepository;
        this.pretRepository = pretRepository;
        this.statusProlongementRepository = statusProlongementRepository;
        this.gestionAdherentService = gestionAdherentService;
        this.pretService = pretService;
        this.dateCalculationService = dateCalculationService;
    }

    @Override
    public List<Prolongement> getAllProlongements() {
        return prolongementRepository.findAll();
    }

    @Override
    public Optional<Prolongement> getProlongementById(Long id) {
        return prolongementRepository.findById(id);
    }

    @Override
    public Prolongement saveProlongement(Prolongement prolongement) {
        return prolongementRepository.save(prolongement);
    }

    @Override
    public void deleteProlongement(Long id) {
        prolongementRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Prolongement createProlongementForPret(Long pretId, int nbJours) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));
        
        // Vérifier qu'il n'y a pas déjà un prolongement pour ce prêt
        if (hasPretActiveProlongement(pret) || hasPretCompletedProlongement(pret)) {
            throw new RuntimeException("Ce prêt a déjà un prolongement actif ou accepté");
        }
        
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setNbJour(nbJours);
        
        Prolongement savedProlongement = prolongementRepository.save(prolongement);
        
        // Créer l'historique initial avec statut EN_ATTENTE
        StatusProlongement statusEnAttente = getStatusByName("EN_ATTENTE");
        
        HistoriqueProlongement historique = new HistoriqueProlongement();
        historique.setProlongement(savedProlongement);
        historique.setStatusProlongement(statusEnAttente);
        historique.setDateChangement(LocalDateTime.now());
        
        historiqueProlongementRepository.save(historique);
        
        return savedProlongement;
    }

    @Override
    @Transactional
    public Prolongement updateStatusProlongement(Long prolongementId, StatusProlongement statusProlongement) {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new RuntimeException("Prolongement not found with id: " + prolongementId));
        
        HistoriqueProlongement historique = new HistoriqueProlongement();
        historique.setProlongement(prolongement);
        historique.setStatusProlongement(statusProlongement);
        historique.setDateChangement(LocalDateTime.now());
        
        historiqueProlongementRepository.save(historique);
        
        return prolongement;
    }
    
    @Override
    public boolean hasPretActiveProlongement(Pret pret) {
        return prolongementRepository.findAll().stream()
                .filter(p -> p.getPret().getId().equals(pret.getId()))
                .anyMatch(p -> getLatestStatusName(p).equals("EN_ATTENTE"));
    }
    
    @Override
    public boolean hasPretCompletedProlongement(Pret pret) {
        return prolongementRepository.findAll().stream()
                .filter(p -> p.getPret().getId().equals(pret.getId()))
                .anyMatch(p -> getLatestStatusName(p).equals("ACCEPTEE"));
    }
    
    @Override
    public Optional<Prolongement> findActiveProlongementByPret(Pret pret) {
        return prolongementRepository.findAll().stream()
                .filter(p -> p.getPret().getId().equals(pret.getId()))
                .filter(p -> getLatestStatusName(p).equals("ACCEPTEE") || getLatestStatusName(p).equals("EN_ATTENTE"))
                .findFirst();
    }
    
    @Override
    public List<Prolongement> findPendingProlongements() {
        return prolongementRepository.findAll().stream()
                .filter(p -> getLatestStatusName(p).equals("EN_ATTENTE"))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Prolongement createProlongementRequest(Pret pret) {
        // Vérifier si le prêt a déjà un prolongement
        if (hasPretActiveProlongement(pret) || hasPretCompletedProlongement(pret)) {
            throw new RuntimeException("Ce prêt a déjà un prolongement actif ou accepté");
        }
        
        // Vérifier le quota de prolongements de l'adhérent
        int prolongementsActifs = prolongementRepository.findAll().stream()
                .filter(p -> p.getPret().getAdherent().getId().equals(pret.getAdherent().getId()))
                .filter(p -> getLatestStatusName(p).equals("ACCEPTEE"))
                .collect(Collectors.toList())
                .size();
        
        int maxProlongements = gestionAdherentService.getNombreProlongementMaxForAdherent(pret.getAdherent());
        if (prolongementsActifs >= maxProlongements) {
            throw new RuntimeException("Vous avez atteint votre quota de " + maxProlongements + " prolongements");
        }
        
        // Déterminer la durée du prolongement (même que la durée de prêt)
        int dureeProlongement = gestionAdherentService.getDureePretForAdherent(pret.getAdherent());
        
        // Créer le prolongement
        return createProlongementForPret(pret.getId(), dureeProlongement);
    }
    
    @Override
    @Transactional
    public boolean acceptProlongement(Long prolongementId) {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new RuntimeException("Prolongement not found with id: " + prolongementId));
        
        // Vérifier que le prolongement est en attente
        if (!isProlongementEnAttente(prolongement)) {
            throw new RuntimeException("Ce prolongement n'est pas en attente de validation");
        }
        
        // Mettre à jour le statut
        StatusProlongement statusAcceptee = getStatusByName("ACCEPTEE");
        updateStatusProlongement(prolongementId, statusAcceptee);
        
        // Mettre à jour la date de retour prévue du prêt en tenant compte des jours non ouvrés
        Pret pret = prolongement.getPret();
        int dureeProlongement = prolongement.getNbJour();
        
        // Calculer la nouvelle date de retour en tenant compte des jours non ouvrés
        LocalDateTime nouvelleDate = dateCalculationService.addJoursOuvres(pret.getDateRetourPrevue(), dureeProlongement);
        
        pret.setDateRetourPrevue(nouvelleDate);
        pretRepository.save(pret);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean rejectProlongement(Long prolongementId) {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new RuntimeException("Prolongement not found with id: " + prolongementId));
        
        // Vérifier que le prolongement est en attente
        if (!isProlongementEnAttente(prolongement)) {
            throw new RuntimeException("Ce prolongement n'est pas en attente de validation");
        }
        
        // Mettre à jour le statut
        StatusProlongement statusRejetee = getStatusByName("REJETEE");
        updateStatusProlongement(prolongementId, statusRejetee);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean cancelProlongement(Long prolongementId) {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new RuntimeException("Prolongement not found with id: " + prolongementId));
        
        // Vérifier que le prolongement est en attente
        if (!isProlongementEnAttente(prolongement)) {
            throw new RuntimeException("Ce prolongement n'est pas en attente de validation");
        }
        
        // Mettre à jour le statut
        StatusProlongement statusAnnulee = getStatusByName("ANNULEE");
        updateStatusProlongement(prolongementId, statusAnnulee);
        
        return true;
    }
    
    @Override
    public String getLatestStatusName(Prolongement prolongement) {
        return historiqueProlongementRepository.findAll().stream()
                .filter(h -> h.getProlongement().getId().equals(prolongement.getId()))
                .max(Comparator.comparing(HistoriqueProlongement::getDateChangement))
                .map(h -> h.getStatusProlongement().getNom())
                .orElse(null);
    }
    
    @Override
    public boolean isProlongementEnAttente(Prolongement prolongement) {
        return "EN_ATTENTE".equals(getLatestStatusName(prolongement));
    }
    
    @Override
    public boolean isProlongementAccepte(Prolongement prolongement) {
        return "ACCEPTEE".equals(getLatestStatusName(prolongement));
    }
    
    // Méthode utilitaire pour obtenir un statut par son nom
    private StatusProlongement getStatusByName(String nom) {
        return statusProlongementRepository.findAll().stream()
                .filter(s -> s.getNom().equals(nom))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Statut " + nom + " non trouvé"));
    }
}
