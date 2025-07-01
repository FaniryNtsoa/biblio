package com.library.service.impl;

import com.library.model.Exemplaire;
import com.library.model.Livre;
import com.library.model.EtatExemplaire;
import com.library.repository.ExemplaireRepository;
import com.library.service.ExemplaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExemplaireServiceImpl implements ExemplaireService {

    private final ExemplaireRepository exemplaireRepository;

    @Autowired
    public ExemplaireServiceImpl(ExemplaireRepository exemplaireRepository) {
        this.exemplaireRepository = exemplaireRepository;
    }

    @Override
    public List<Exemplaire> getAllExemplaires() {
        return exemplaireRepository.findAll();
    }

    @Override
    public Optional<Exemplaire> getExemplaireById(Long id) {
        return exemplaireRepository.findById(id);
    }

    @Override
    public Exemplaire saveExemplaire(Exemplaire exemplaire) {
        return exemplaireRepository.save(exemplaire);
    }

    @Override
    public void deleteExemplaire(Long id) {
        exemplaireRepository.deleteById(id);
    }

    @Override
    public List<Exemplaire> findByLivre(Livre livre) {
        return exemplaireRepository.findByLivre(livre);
    }

    @Override
    public List<Exemplaire> findByEtatExemplaire(EtatExemplaire etatExemplaire) {
        return exemplaireRepository.findByEtatExemplaire(etatExemplaire);
    }

    @Override
    public long countByLivre(Livre livre) {
        return exemplaireRepository.countByLivre(livre);
    }

    @Override
    @Transactional
    public Exemplaire updateEtatExemplaire(Long exemplaireId, EtatExemplaire etatExemplaire) {
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId)
                .orElseThrow(() -> new RuntimeException("Exemplaire not found with id: " + exemplaireId));
        
        exemplaire.setEtatExemplaire(etatExemplaire);
        return exemplaireRepository.save(exemplaire);
    }

    @Override
    @Transactional
    public List<Exemplaire> createMultipleExemplaires(Livre livre, EtatExemplaire etatExemplaire, int count) {
        List<Exemplaire> exemplaires = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            Exemplaire exemplaire = new Exemplaire();
            exemplaire.setLivre(livre);
            exemplaire.setEtatExemplaire(etatExemplaire);
            exemplaires.add(exemplaireRepository.save(exemplaire));
        }
        
        return exemplaires;
    }
}
