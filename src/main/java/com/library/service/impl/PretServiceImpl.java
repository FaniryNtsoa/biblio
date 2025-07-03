package com.library.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.model.Adherent;
import com.library.model.Pret;
import com.library.model.TypePret;
import com.library.repository.PretRepository;
import com.library.service.PretService;

@Service
public class PretServiceImpl implements PretService {

    private final PretRepository pretRepository;

    @Autowired
    public PretServiceImpl(PretRepository pretRepository) {
        this.pretRepository = pretRepository;
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
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().equals(adherent))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pret> findByTypePret(TypePret typePret) {
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getTypePret().equals(typePret))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pret> findByDatePretBetween(LocalDate dateDebut, LocalDate dateFin) {
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> !pret.getDatePret().isBefore(dateDebut) && !pret.getDatePret().isAfter(dateFin))
                .collect(Collectors.toList());
    }

    @Override
    public long countByAdherent(Adherent adherent) {
        // Implémentation sans méthode personnalisée dans le repository
        return pretRepository.findAll().stream()
                .filter(pret -> pret.getAdherent().equals(adherent))
                .count();
    }

}
