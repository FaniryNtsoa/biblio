package com.library.service.impl;

import com.library.model.HistoriquePret;
import com.library.model.Pret;
import com.library.model.StatusPret;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.PretRepository;
import com.library.repository.StatusPretRepository;
import com.library.service.HistoriquePretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriquePretServiceImpl implements HistoriquePretService {

    private final HistoriquePretRepository historiquePretRepository;
    private final PretRepository pretRepository;
    private final StatusPretRepository statusPretRepository;

    @Autowired
    public HistoriquePretServiceImpl(HistoriquePretRepository historiquePretRepository,
                                   PretRepository pretRepository,
                                   StatusPretRepository statusPretRepository) {
        this.historiquePretRepository = historiquePretRepository;
        this.pretRepository = pretRepository;
        this.statusPretRepository = statusPretRepository;
    }

    @Override
    public List<HistoriquePret> getAllHistoriquePrets() {
        return historiquePretRepository.findAll();
    }

    @Override
    public Optional<HistoriquePret> getHistoriquePretById(Long id) {
        return historiquePretRepository.findById(id);
    }

    @Override
    public HistoriquePret saveHistoriquePret(HistoriquePret historiquePret) {
        return historiquePretRepository.save(historiquePret);
    }

    @Override
    public void deleteHistoriquePret(Long id) {
        historiquePretRepository.deleteById(id);
    }

    @Override
    public List<HistoriquePret> findByPret(Pret pret) {
        return historiquePretRepository.findByPret(pret);
    }

    @Override
    public List<HistoriquePret> findByStatusPret(StatusPret statusPret) {
        return historiquePretRepository.findByStatusPret(statusPret);
    }

    @Override
    public List<HistoriquePret> findByDateRetourBetween(LocalDate dateDebut, LocalDate dateFin) {
        return historiquePretRepository.findByDateRetourBetween(dateDebut, dateFin);
    }

    @Override
    @Transactional
    public HistoriquePret createHistoriquePret(Long pretId, Long statusPretId, LocalDate dateRetour) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));
        
        StatusPret statusPret = statusPretRepository.findById(statusPretId)
                .orElseThrow(() -> new RuntimeException("StatusPret not found with id: " + statusPretId));
        
        HistoriquePret historique = new HistoriquePret();
        historique.setPret(pret);
        historique.setStatusPret(statusPret);
        historique.setDateRetour(dateRetour);
        
        return historiquePretRepository.save(historique);
    }
}
