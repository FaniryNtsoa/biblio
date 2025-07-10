package com.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.library.model.HistoriquePret;
import com.library.model.Pret;

public interface HistoriquePretService {
    List<HistoriquePret> getAllHistoriquePrets();
    Optional<HistoriquePret> getHistoriquePretById(Long id);
    HistoriquePret saveHistoriquePret(HistoriquePret historiquePret);
    void deleteHistoriquePret(Long id);
    HistoriquePret createHistoriquePret(Long pretId, Long statusPretId, LocalDateTime dateRetour);
    List<HistoriquePret> findByPret(Pret pret); // Ajouter cette méthode
}
