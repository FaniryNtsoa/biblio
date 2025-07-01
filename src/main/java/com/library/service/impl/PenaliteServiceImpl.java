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
import java.util.List;
import java.util.Optional;

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
        return penaliteRepository.findByPret(pret);
    }

    @Override
    public List<Penalite> findByDatePenaliteBetween(LocalDate dateDebut, LocalDate dateFin) {
        return penaliteRepository.findByDatePenaliteBetween(dateDebut, dateFin);
    }

    @Override
    @Transactional
    public Penalite createPenaliteForPret(Long pretId, String description) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));
        
        Penalite penalite = new Penalite();
        penalite.setPret(pret);
        penalite.setDatePenalite(LocalDate.now());
        penalite.setDescription(description);
        
        return penaliteRepository.save(penalite);
    }
}
