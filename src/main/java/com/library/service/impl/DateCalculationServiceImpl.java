package com.library.service.impl;

import com.library.model.JourFerie;
import com.library.repository.JourFerieRepository;
import com.library.service.DateCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DateCalculationServiceImpl implements DateCalculationService {

    private final JourFerieRepository jourFerieRepository;

    @Autowired
    public DateCalculationServiceImpl(JourFerieRepository jourFerieRepository) {
        this.jourFerieRepository = jourFerieRepository;
    }

    @Override
    public boolean isJourOuvre(LocalDate date) {
        // Vérifier si c'est un dimanche
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }
        
        // Vérifier si c'est un jour férié
        return !jourFerieRepository.existsByDate(date);
    }

    @Override
    public boolean isJourOuvre(LocalDateTime dateTime) {
        return isJourOuvre(dateTime.toLocalDate());
    }

    @Override
    public LocalDate getNextJourOuvre(LocalDate date) {
        LocalDate nextDate = date;
        // Si la date fournie est un jour non ouvré, on cherche le prochain jour ouvré
        if (!isJourOuvre(nextDate)) {
            do {
                nextDate = nextDate.plusDays(1);
            } while (!isJourOuvre(nextDate));
        }
        return nextDate;
    }

    @Override
    public LocalDateTime getNextJourOuvre(LocalDateTime dateTime) {
        LocalDate nextDate = getNextJourOuvre(dateTime.toLocalDate());
        return LocalDateTime.of(nextDate, dateTime.toLocalTime());
    }

    @Override
    public LocalDate addJoursOuvres(LocalDate date, int nombreJours) {
        // D'abord, calculer la date normale en ajoutant simplement le nombre de jours
        LocalDate result = date.plusDays(nombreJours);
        
        // Ensuite, vérifier si cette date tombe sur un jour non ouvré (dimanche ou jour férié)
        // Si oui, décaler au prochain jour ouvré
        return getNextJourOuvre(result);
    }

    @Override
    public LocalDateTime addJoursOuvres(LocalDateTime dateTime, int nombreJours) {
        // Calculer d'abord la date normale en ajoutant simplement le nombre de jours
        LocalDateTime result = dateTime.plusDays(nombreJours);
        
        // Ensuite, vérifier si cette date tombe sur un jour non ouvré
        // Si oui, décaler au prochain jour ouvré
        LocalDate resultDate = getNextJourOuvre(result.toLocalDate());
        return LocalDateTime.of(resultDate, dateTime.toLocalTime());
    }

    @Override
    public List<JourFerie> getAllJoursFeries() {
        return jourFerieRepository.findAll();
    }

    @Override
    @Transactional
    public JourFerie ajouterJourFerie(LocalDate date, String nom, String description) {
        if (jourFerieRepository.existsByDate(date)) {
            throw new IllegalArgumentException("Un jour férié existe déjà à cette date");
        }
        
        JourFerie jourFerie = new JourFerie();
        jourFerie.setDate(date);
        jourFerie.setNom(nom);
        jourFerie.setDescription(description);
        
        return jourFerieRepository.save(jourFerie);
    }

    @Override
    @Transactional
    public void supprimerJourFerie(Long id) {
        jourFerieRepository.deleteById(id);
    }
}

