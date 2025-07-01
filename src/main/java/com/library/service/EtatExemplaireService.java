package com.library.service;

import com.library.model.EtatExemplaire;
import java.util.List;
import java.util.Optional;

public interface EtatExemplaireService {
    List<EtatExemplaire> getAllEtatExemplaires();
    Optional<EtatExemplaire> getEtatExemplaireById(Long id);
    EtatExemplaire saveEtatExemplaire(EtatExemplaire etatExemplaire);
    void deleteEtatExemplaire(Long id);
    Optional<EtatExemplaire> findByNom(String nom);
}
