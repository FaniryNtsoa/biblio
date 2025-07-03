package com.library.service.impl;

import com.library.model.Livre;
import com.library.model.Categorie;
import com.library.repository.LivreRepository;
import com.library.repository.CategorieRepository;
import com.library.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;
    private final CategorieRepository categorieRepository;

    @Autowired
    public LivreServiceImpl(LivreRepository livreRepository, 
                           CategorieRepository categorieRepository) {
        this.livreRepository = livreRepository;
        this.categorieRepository = categorieRepository;
    }

    @Override
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    @Override
    public Optional<Livre> getLivreById(Long id) {
        return livreRepository.findById(id);
    }

    @Override
    public Livre saveLivre(Livre livre) {
        return livreRepository.save(livre);
    }

    @Override
    public void deleteLivre(Long id) {
        livreRepository.deleteById(id);
    }

    @Override
    public List<Livre> findByTitre(String titre) {
        // Implémentation sans méthode personnalisée dans le repository
        return livreRepository.findAll().stream()
                .filter(livre -> livre.getTitre().toLowerCase().contains(titre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livre> findByAuteur(String auteur) {
        // Implémentation sans méthode personnalisée dans le repository
        return livreRepository.findAll().stream()
                .filter(livre -> livre.getAuteur().toLowerCase().contains(auteur.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livre> findByAgeMin(Integer age) {
        // Implémentation sans méthode personnalisée dans le repository
        return livreRepository.findAll().stream()
                .filter(livre -> livre.getAgeMin() <= age)
                .collect(Collectors.toList());
    }

    @Override
    public List<Livre> findByLangue(String langue) {
        // Implémentation sans méthode personnalisée dans le repository
        return livreRepository.findAll().stream()
                .filter(livre -> livre.getLangue().equalsIgnoreCase(langue))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Livre addCategoriesToLivre(Long livreId, List<Long> categorieIds) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre not found with id: " + livreId));

        List<Categorie> categories = categorieRepository.findAllById(categorieIds);
        livre.getCategories().addAll(categories);

        return livreRepository.save(livre);
    }

    @Override
    @Transactional
    public Livre removeCategorieFromLivre(Long livreId, Long categorieId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre not found with id: " + livreId));

        livre.setCategories(livre.getCategories().stream()
                .filter(c -> !c.getId().equals(categorieId))
                .collect(Collectors.toSet()));

        return livreRepository.save(livre);
    }

}
