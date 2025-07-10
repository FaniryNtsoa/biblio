package com.library.service.impl;

import com.library.model.Inscription;
import com.library.model.Adherent;
import com.library.repository.InscriptionRepository;
import com.library.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;

    @Autowired
    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    @Override
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    @Override
    public Optional<Inscription> getInscriptionById(Long id) {
        return inscriptionRepository.findById(id);
    }

    @Override
    public Inscription saveInscription(Inscription inscription) {
        return inscriptionRepository.save(inscription);
    }

    @Override
    public void deleteInscription(Long id) {
        inscriptionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Inscription renewInscription(Long inscriptionId, LocalDateTime newExpirationDate) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new RuntimeException("Inscription not found with id: " + inscriptionId));
        
        inscription.setDateExpiration(newExpirationDate);
        return inscriptionRepository.save(inscription);
    }
    
    @Override
    public boolean isAdherentActiveMember(Adherent adherent) {
        return findLatestInscriptionByAdherent(adherent)
                .map(inscription -> !LocalDateTime.now().isAfter(inscription.getDateExpiration()))
                .orElse(false);
    }
    
    @Override
    @Transactional
    public Inscription createNewInscription(Adherent adherent, LocalDateTime dateExpiration) {
        Inscription inscription = new Inscription();
        inscription.setAdherent(adherent);
        
        // Utiliser la date actuelle comme date d'inscription
        LocalDateTime today = LocalDateTime.now();
        inscription.setDateInscription(today);
        
        // Utiliser la date d'expiration fournie
        inscription.setDateExpiration(dateExpiration);
        
        return inscriptionRepository.save(inscription);
    }
    
    @Override
    @Transactional
    public Inscription createNewInscription(Adherent adherent, int durationInMonths) {
        // Utiliser la date actuelle comme date d'inscription
        LocalDateTime today = LocalDateTime.now();
        
        // Calculer la date d'expiration basée sur la durée en mois
        LocalDateTime expirationDate = today.plusMonths(durationInMonths);
        
        // Utiliser la nouvelle méthode avec la date d'expiration calculée
        return createNewInscription(adherent, expirationDate);
    }
    
    @Override
    public Optional<Inscription> findLatestInscriptionByAdherent(Adherent adherent) {
        List<Inscription> inscriptions = findInscriptionsByAdherent(adherent);
        if (inscriptions.isEmpty()) {
            return Optional.empty();
        }
        
        // Trouver l'inscription avec la date d'expiration la plus récente
        return inscriptions.stream()
                .max(Comparator.comparing(Inscription::getDateExpiration));
    }
    
    @Override
    public List<Inscription> findInscriptionsByAdherent(Adherent adherent) {
        return inscriptionRepository.findAll().stream()
                .filter(inscription -> inscription.getAdherent().getId().equals(adherent.getId()))
                .collect(Collectors.toList());
    }
}
