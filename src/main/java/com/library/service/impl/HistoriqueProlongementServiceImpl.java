package com.library.service.impl;

import com.library.model.HistoriqueProlongement;
import com.library.model.Prolongement;
import com.library.model.StatusProlongement;
import com.library.repository.HistoriqueProlongementRepository;
import com.library.repository.ProlongementRepository;
import com.library.repository.StatusProlongementRepository;
import com.library.service.HistoriqueProlongementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueProlongementServiceImpl implements HistoriqueProlongementService {

    private final HistoriqueProlongementRepository historiqueProlongementRepository;
    private final ProlongementRepository prolongementRepository;
    private final StatusProlongementRepository statusProlongementRepository;

    @Autowired
    public HistoriqueProlongementServiceImpl(HistoriqueProlongementRepository historiqueProlongementRepository,
                                           ProlongementRepository prolongementRepository,
                                           StatusProlongementRepository statusProlongementRepository) {
        this.historiqueProlongementRepository = historiqueProlongementRepository;
        this.prolongementRepository = prolongementRepository;
        this.statusProlongementRepository = statusProlongementRepository;
    }

    @Override
    public List<HistoriqueProlongement> getAllHistoriqueProlongements() {
        return historiqueProlongementRepository.findAll();
    }

    @Override
    public Optional<HistoriqueProlongement> getHistoriqueProlongementById(Long id) {
        return historiqueProlongementRepository.findById(id);
    }

    @Override
    public HistoriqueProlongement saveHistoriqueProlongement(HistoriqueProlongement historiqueProlongement) {
        return historiqueProlongementRepository.save(historiqueProlongement);
    }

    @Override
    public void deleteHistoriqueProlongement(Long id) {
        historiqueProlongementRepository.deleteById(id);
    }

    @Override
    public List<HistoriqueProlongement> findByProlongement(Prolongement prolongement) {
        return historiqueProlongementRepository.findByProlongement(prolongement);
    }

    @Override
    public List<HistoriqueProlongement> findByStatusProlongement(StatusProlongement statusProlongement) {
        return historiqueProlongementRepository.findByStatusProlongement(statusProlongement);
    }

    @Override
    @Transactional
    public HistoriqueProlongement createHistoriqueProlongement(Long prolongementId, Long statusProlongementId) {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new RuntimeException("Prolongement not found with id: " + prolongementId));
        
        StatusProlongement statusProlongement = statusProlongementRepository.findById(statusProlongementId)
                .orElseThrow(() -> new RuntimeException("StatusProlongement not found with id: " + statusProlongementId));
        
        HistoriqueProlongement historique = new HistoriqueProlongement();
        historique.setProlongement(prolongement);
        historique.setStatusProlongement(statusProlongement);
        
        return historiqueProlongementRepository.save(historique);
    }
}
