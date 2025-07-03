package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.model.HistoriqueProlongement;

public interface HistoriqueProlongementService {
    List<HistoriqueProlongement> getAllHistoriqueProlongements();
    Optional<HistoriqueProlongement> getHistoriqueProlongementById(Long id);
    HistoriqueProlongement saveHistoriqueProlongement(HistoriqueProlongement historiqueProlongement);
    void deleteHistoriqueProlongement(Long id);
    HistoriqueProlongement createHistoriqueProlongement(Long prolongementId, Long statusProlongementId);
}
