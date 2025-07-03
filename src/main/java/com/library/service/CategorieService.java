package com.library.service;

import com.library.model.Categorie;
import java.util.List;
import java.util.Optional;

public interface CategorieService {
    List<Categorie> getAllCategories();
    Optional<Categorie> getCategorieById(Long id);
    Categorie saveCategorie(Categorie categorie);
    void deleteCategorie(Long id);
}
