package com.library.service;

import com.library.model.Exemplaire;
import com.library.model.Livre;
import com.library.model.EtatExemplaire;
import java.util.List;
import java.util.Optional;

public interface ExemplaireService {
    List<Exemplaire> getAllExemplaires();
    Optional<Exemplaire> getExemplaireById(Long id);
    Exemplaire saveExemplaire(Exemplaire exemplaire);
    void deleteExemplaire(Long id);
    List<Exemplaire> findByLivre(Livre livre);
    List<Exemplaire> findByEtatExemplaire(EtatExemplaire etatExemplaire);
    long countByLivre(Livre livre);
    Exemplaire updateEtatExemplaire(Long exemplaireId, EtatExemplaire etatExemplaire);
    List<Exemplaire> createMultipleExemplaires(Livre livre, EtatExemplaire etatExemplaire, int count);
}
