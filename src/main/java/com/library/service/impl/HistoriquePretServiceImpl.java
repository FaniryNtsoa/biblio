package com.library.service.impl;

import com.library.model.HistoriquePret;
import com.library.model.Pret;
import com.library.model.StatusPret;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.PretRepository;
import com.library.repository.StatusPretRepository;
import com.library.service.HistoriquePretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoriquePretServiceImpl implements HistoriquePretService {

    private final HistoriquePretRepository historiquePretRepository;
    private final PretRepository pretRepository;
    private final StatusPretRepository statusPretRepository;

    @Autowired
    public HistoriquePretServiceImpl(HistoriquePretRepository historiquePretRepository,
                                   PretRepository pretRepository,
                                   StatusPretRepository statusPretRepository) {
        this.historiquePretRepository = historiquePretRepository;
        this.pretRepository = pretRepository;
        this.statusPretRepository = statusPretRepository;
    }

    @Override
    public List<HistoriquePret> getAllHistoriquePrets() {
        return historiquePretRepository.findAll();
    }

    @Override
    public Optional<HistoriquePret> getHistoriquePretById(Long id) {
        return historiquePretRepository.findById(id);
    }

    @Override
    public HistoriquePret saveHistoriquePret(HistoriquePret historiquePret) {
        return historiquePretRepository.save(historiquePret);
    }

    @Override
    public void deleteHistoriquePret(Long id) {
        historiquePretRepository.deleteById(id);
    }

    @Override
    @Transactional
    public HistoriquePret createHistoriquePret(Long pretId, Long statusPretId, LocalDateTime dateRetour) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));
        
        StatusPret statusPret = statusPretRepository.findById(statusPretId)
                .orElseThrow(() -> new RuntimeException("StatusPret not found with id: " + statusPretId));
        
        HistoriquePret historique = new HistoriquePret();
        historique.setPret(pret);
        historique.setStatusPret(statusPret);
        historique.setExemplaire(pret.getExemplaire());
        historique.setDateRetour(dateRetour);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire("Changement de statut vers: " + statusPret.getNom());
        
        return historiquePretRepository.save(historique);
    }

    @Override
    public List<HistoriquePret> findByPret(Pret pret) {
        return historiquePretRepository.findAll().stream()
                .filter(h -> h.getPret().getId().equals(pret.getId()))
                .collect(Collectors.toList());
    }
}
