package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.model.Prolongement;
import com.library.model.StatusProlongement;

public interface ProlongementService {
    List<Prolongement> getAllProlongements();
    Optional<Prolongement> getProlongementById(Long id);
    Prolongement saveProlongement(Prolongement prolongement);
    void deleteProlongement(Long id);
    Prolongement createProlongementForPret(Long pretId, int nbJours);
    Prolongement updateStatusProlongement(Long prolongementId, StatusProlongement statusProlongement);
}
