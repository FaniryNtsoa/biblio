package com.library.service.impl;

import com.library.model.Prolongement;
import com.library.model.HistoriqueProlongement;
import com.library.model.Pret;
import com.library.model.StatusProlongement;
import com.library.repository.ProlongementRepository;
import com.library.repository.HistoriqueProlongementRepository;
import com.library.repository.PretRepository;
import com.library.service.ProlongementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProlongementServiceImpl implements ProlongementService {

    private final ProlongementRepository prolongementRepository;
    private final HistoriqueProlongementRepository historiqueProlongementRepository;
    private final PretRepository pretRepository;

    @Autowired
    public ProlongementServiceImpl(ProlongementRepository prolongementRepository,
                                  HistoriqueProlongementRepository historiqueProlongementRepository,
                                  PretRepository pretRepository) {
        this.prolongementRepository = prolongementRepository;
        this.historiqueProlongementRepository = historiqueProlongementRepository;
        this.pretRepository = pretRepository;
    }

    @Override
    public List<Prolongement> getAllProlongements() {
        return prolongementRepository.findAll();
    }

    @Override
    public Optional<Prolongement> getProlongementById(Long id) {
        return prolongementRepository.findById(id);
    }

    @Override
    public Prolongement saveProlongement(Prolongement prolongement) {
        return prolongementRepository.save(prolongement);
    }

    @Override
    public void deleteProlongement(Long id) {
        prolongementRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Prolongement createProlongementForPret(Long pretId, int nbJours) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));
        
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setNbJour(nbJours);
        
        return prolongementRepository.save(prolongement);
    }

    @Override
    @Transactional
    public Prolongement updateStatusProlongement(Long prolongementId, StatusProlongement statusProlongement) {
        Prolongement prolongement = prolongementRepository.findById(prolongementId)
                .orElseThrow(() -> new RuntimeException("Prolongement not found with id: " + prolongementId));
        
        HistoriqueProlongement historique = new HistoriqueProlongement();
        historique.setProlongement(prolongement);
        historique.setStatusProlongement(statusProlongement);
        
        historiqueProlongementRepository.save(historique);
        
        return prolongement;
    }
}
