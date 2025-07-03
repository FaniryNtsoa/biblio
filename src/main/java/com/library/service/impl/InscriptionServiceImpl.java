package com.library.service.impl;

import com.library.model.Inscription;
import com.library.repository.InscriptionRepository;
import com.library.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public Inscription renewInscription(Long inscriptionId, LocalDate newExpirationDate) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new RuntimeException("Inscription not found with id: " + inscriptionId));
        
        inscription.setDateExpiration(newExpirationDate);
        return inscriptionRepository.save(inscription);
    }
}
