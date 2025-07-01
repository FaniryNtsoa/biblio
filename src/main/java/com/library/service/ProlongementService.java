package com.library.service;

import com.library.model.Prolongement;
import com.library.model.Pret;
import com.library.model.StatusProlongement;
import java.util.List;
import java.util.Optional;

public interface ProlongementService {
    List<Prolongement> getAllProlongements();
    Optional<Prolongement> getProlongementById(Long id);
    Prolongement saveProlongement(Prolongement prolongement);
    void deleteProlongement(Long id);
    List<Prolongement> findByPret(Pret pret);
    long countByPret(Pret pret);
    Prolongement createProlongementForPret(Long pretId, int nbJours);
    Prolongement updateStatusProlongement(Long prolongementId, StatusProlongement statusProlongement);
}
