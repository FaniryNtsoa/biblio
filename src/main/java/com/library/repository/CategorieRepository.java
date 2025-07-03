package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}
