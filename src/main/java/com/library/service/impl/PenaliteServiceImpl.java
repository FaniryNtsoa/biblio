package com.library.service.impl;

import com.library.model.Penalite;
import com.library.model.Pret;
import com.library.repository.PenaliteRepository;
import com.library.repository.PretRepository;
import com.library.service.PenaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
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
    public List<Penalite> findByDateBetween(LocalDate dateDebut, LocalDate dateFin) {
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
        penalite.setDatePenalite(LocalDate.now());
        
        return penaliteRepository.save(penalite);
    }
    
    @Override
    public List<Penalite> getRecentPenalites(int limit) {
        return penaliteRepository.findAll().stream()
                .sorted(Comparator.comparing(Penalite::getDatePenalite).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
