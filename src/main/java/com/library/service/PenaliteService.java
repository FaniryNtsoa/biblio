package com.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.library.model.Penalite;
import com.library.model.Pret;

public interface PenaliteService {
    List<Penalite> getAllPenalites();
    Optional<Penalite> getPenaliteById(Long id);
    Penalite savePenalite(Penalite penalite);
    void deletePenalite(Long id);
    List<Penalite> findByPret(Pret pret);
    List<Penalite> findByDateBetween(LocalDate dateDebut, LocalDate dateFin);
    Penalite createPenaliteForPret(Long pretId, String motif);
    List<Penalite> getRecentPenalites(int limit);
}
