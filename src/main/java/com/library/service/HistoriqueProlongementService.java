package com.library.service;

import com.library.model.HistoriqueProlongement;
import com.library.model.Prolongement;
import com.library.model.StatusProlongement;
import java.util.List;
import java.util.Optional;

public interface HistoriqueProlongementService {
    List<HistoriqueProlongement> getAllHistoriqueProlongements();
    Optional<HistoriqueProlongement> getHistoriqueProlongementById(Long id);
    HistoriqueProlongement saveHistoriqueProlongement(HistoriqueProlongement historiqueProlongement);
    void deleteHistoriqueProlongement(Long id);
    List<HistoriqueProlongement> findByProlongement(Prolongement prolongement);
    List<HistoriqueProlongement> findByStatusProlongement(StatusProlongement statusProlongement);
    HistoriqueProlongement createHistoriqueProlongement(Long prolongementId, Long statusProlongementId);
}
