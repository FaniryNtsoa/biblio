package com.library.service.impl;

import com.library.model.EtatExemplaire;
import com.library.repository.EtatExemplaireRepository;
import com.library.service.EtatExemplaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EtatExemplaireServiceImpl implements EtatExemplaireService {

    private final EtatExemplaireRepository etatExemplaireRepository;

    @Autowired
    public EtatExemplaireServiceImpl(EtatExemplaireRepository etatExemplaireRepository) {
        this.etatExemplaireRepository = etatExemplaireRepository;
    }

    @Override
    public List<EtatExemplaire> getAllEtatExemplaires() {
        return etatExemplaireRepository.findAll();
    }

    @Override
    public Optional<EtatExemplaire> getEtatExemplaireById(Long id) {
        return etatExemplaireRepository.findById(id);
    }

    @Override
    public EtatExemplaire saveEtatExemplaire(EtatExemplaire etatExemplaire) {
        return etatExemplaireRepository.save(etatExemplaire);
    }

    @Override
    public void deleteEtatExemplaire(Long id) {
        etatExemplaireRepository.deleteById(id);
    }
}
