package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.model.Livre;

public interface LivreService {
    List<Livre> getAllLivres();
    Optional<Livre> getLivreById(Long id);
    Livre saveLivre(Livre livre);
    void deleteLivre(Long id);
    
    // Méthodes de recherche et filtrage
    List<Livre> findByTitre(String titre);
    List<Livre> findByAuteur(String auteur);
    List<Livre> findByAgeMin(Integer age);
    List<Livre> findByLangue(String langue);
    
    // Méthodes pour gérer les relations
    Livre addCategoriesToLivre(Long livreId, List<Long> categorieIds);
    Livre removeCategorieFromLivre(Long livreId, Long categorieId);
}
