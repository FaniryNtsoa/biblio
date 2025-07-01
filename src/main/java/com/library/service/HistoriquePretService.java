package com.library.service;

import com.library.model.HistoriquePret;
import com.library.model.Pret;
import com.library.model.StatusPret;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistoriquePretService {
    List<HistoriquePret> getAllHistoriquePrets();
    Optional<HistoriquePret> getHistoriquePretById(Long id);
    HistoriquePret saveHistoriquePret(HistoriquePret historiquePret);
    void deleteHistoriquePret(Long id);
    List<HistoriquePret> findByPret(Pret pret);
    List<HistoriquePret> findByStatusPret(StatusPret statusPret);
    List<HistoriquePret> findByDateRetourBetween(LocalDate dateDebut, LocalDate dateFin);
    HistoriquePret createHistoriquePret(Long pretId, Long statusPretId, LocalDate dateRetour);
}
