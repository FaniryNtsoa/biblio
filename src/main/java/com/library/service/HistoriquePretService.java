package com.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.library.model.HistoriquePret;

public interface HistoriquePretService {
    List<HistoriquePret> getAllHistoriquePrets();
    Optional<HistoriquePret> getHistoriquePretById(Long id);
    HistoriquePret saveHistoriquePret(HistoriquePret historiquePret);
    void deleteHistoriquePret(Long id);
    HistoriquePret createHistoriquePret(Long pretId, Long statusPretId, LocalDate dateRetour);
}
