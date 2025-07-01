package com.library.service;

import com.library.model.Livre;
import java.util.List;
import java.util.Optional;

public interface LivreService {
    List<Livre> getAllLivres();
    Optional<Livre> getLivreById(Long id);
    Livre saveLivre(Livre livre);
    void deleteLivre(Long id);
    List<Livre> findByTitre(String titre);
    List<Livre> findByAuteur(String auteur);
    List<Livre> findByAgeMin(Integer age);
    List<Livre> findByLangue(String langue);
    Livre addCategoriesToLivre(Long livreId, List<Long> categorieIds);
    Livre removeCategorieFromLivre(Long livreId, Long categorieId);
}
