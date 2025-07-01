package com.library.service.impl;

import com.library.model.Pret;
import com.library.model.Adherent;
import com.library.model.TypePret;
import com.library.model.StatusPret;
import com.library.model.HistoriquePret;
import com.library.model.Prolongement;
import com.library.repository.PretRepository;
import com.library.repository.HistoriquePretRepository;
import com.library.repository.ProlongementRepository;
import com.library.service.PretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PretServiceImpl implements PretService {

    private final PretRepository pretRepository;
    private final HistoriquePretRepository historiquePretRepository;
    private final ProlongementRepository prolongementRepository;

    @Autowired
    public PretServiceImpl(PretRepository pretRepository, 
                           HistoriquePretRepository historiquePretRepository,
                           ProlongementRepository prolongementRepository) {
        this.pretRepository = pretRepository;
        this.historiquePretRepository = historiquePretRepository;
        this.prolongementRepository = prolongementRepository;
    }

    @Override
    public List<Pret> getAllPrets() {
        return pretRepository.findAll();
    }

    @Override
    public Optional<Pret> getPretById(Long id) {
        return pretRepository.findById(id);
    }

    @Override
    public Pret savePret(Pret pret) {
        return pretRepository.save(pret);
    }

    @Override
    public void deletePret(Long id) {
        pretRepository.deleteById(id);
    }

    @Override
    public List<Pret> findByAdherent(Adherent adherent) {
        return pretRepository.findByAdherent(adherent);
    }

    @Override
    public List<Pret> findByTypePret(TypePret typePret) {
        return pretRepository.findByTypePret(typePret);
    }

    @Override
    public List<Pret> findByDatePretBetween(LocalDate dateDebut, LocalDate dateFin) {
        return pretRepository.findByDatePretBetween(dateDebut, dateFin);
    }

    @Override
    public long countByAdherent(Adherent adherent) {
        return pretRepository.countByAdherent(adherent);
    }

    @Override
    @Transactional
    public Pret updateStatusPret(Long pretId, StatusPret statusPret, LocalDate dateRetour) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));

        HistoriquePret historique = new HistoriquePret();
        historique.setPret(pret);
        historique.setStatusPret(statusPret);
        historique.setDateRetour(dateRetour);
        
        historiquePretRepository.save(historique);
        
        return pret;
    }

    @Override
    @Transactional
    public Pret prolongerPret(Long pretId, int nbJours) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt not found with id: " + pretId));

        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setNbJour(nbJours);
        
        prolongementRepository.save(prolongement);
        
        return pret;
    }
}
