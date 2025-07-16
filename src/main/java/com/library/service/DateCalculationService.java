package com.library.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.library.model.JourFerie;

/**
 * Service pour calculer les dates en tenant compte des jours non ouvrés
 * (dimanches et jours fériés)
 */
public interface DateCalculationService {
    /**
     * Vérifie si une date est un jour ouvré (ni dimanche, ni jour férié)
     */
    boolean isJourOuvre(LocalDate date);
    
    /**
     * Vérifie si une date est un jour ouvré (ni dimanche, ni jour férié)
     */
    boolean isJourOuvre(LocalDateTime dateTime);
    
    /**
     * Retourne le prochain jour ouvré à partir d'une date
     */
    LocalDate getNextJourOuvre(LocalDate date);
    
    /**
     * Retourne le prochain jour ouvré à partir d'une date et heure
     */
    LocalDateTime getNextJourOuvre(LocalDateTime dateTime);
    
    /**
     * Ajoute un nombre de jours ouvrés à une date
     */
    LocalDate addJoursOuvres(LocalDate date, int nombreJours);
    
    /**
     * Ajoute un nombre de jours ouvrés à une date et heure
     */
    LocalDateTime addJoursOuvres(LocalDateTime dateTime, int nombreJours);
    
    /**
     * Récupère tous les jours fériés
     */
    List<JourFerie> getAllJoursFeries();
    
    /**
     * Ajoute un jour férié
     */
    JourFerie ajouterJourFerie(LocalDate date, String nom, String description);
    
    /**
     * Supprime un jour férié
     */
    void supprimerJourFerie(Long id);
}
