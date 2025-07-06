package com.library.service.impl;

import com.library.model.Exemplaire;
import com.library.model.Livre;
import com.library.model.StatusPret;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.StatusPretRepository;
import com.library.service.ExemplaireAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExemplaireAvailabilityServiceImpl implements ExemplaireAvailabilityService {

    private final HistoriquePretRepository historiquePretRepository;
    private final StatusPretRepository statusPretRepository;

    @Autowired
    public ExemplaireAvailabilityServiceImpl(HistoriquePretRepository historiquePretRepository,
                                           StatusPretRepository statusPretRepository) {
        this.historiquePretRepository = historiquePretRepository;
        this.statusPretRepository = statusPretRepository;
    }

    @Override
    public List<Exemplaire> getAvailableExemplaires(Livre livre) {
        return livre.getExemplaires().stream()
                .filter(this::isExemplaireAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Exemplaire getFirstAvailableExemplaire(Livre livre) {
        return getAvailableExemplaires(livre).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isExemplaireAvailable(Exemplaire exemplaire) {
        StatusPret statusEnCours = statusPretRepository.findAll().stream()
                .filter(s -> s.getNom().equals("EN_COURS"))
                .findFirst()
                .orElse(null);
        
        if (statusEnCours == null) {
            return true; // Si pas de statut, considérer comme disponible
        }
        
        // Vérifier si cet exemplaire est actuellement emprunté
        return historiquePretRepository.findAll().stream()
                .filter(h -> h.getExemplaire().getId().equals(exemplaire.getId()))
                .filter(h -> h.getStatusPret().getId().equals(statusEnCours.getId()))
                .noneMatch(h -> historiquePretRepository.findAll().stream()
                    .noneMatch(h2 -> h2.getPret().getId().equals(h.getPret().getId()) && 
                               h2.getDateChangement().isAfter(h.getDateChangement()) &&
                               (h2.getStatusPret().getNom().equals("RENDU") || h2.getStatusPret().getNom().equals("EN_RETARD"))));
    }
}
