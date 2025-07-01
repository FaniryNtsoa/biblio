package com.library.service;

import com.library.model.Adherent;
import com.library.model.TypeAdherent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdherentService {
    List<Adherent> getAllAdherents();
    Optional<Adherent> getAdherentById(Long id);
    Adherent saveAdherent(Adherent adherent);
    void deleteAdherent(Long id);
    List<Adherent> findByNom(String nom);
    List<Adherent> findByPrenom(String prenom);
    List<Adherent> findByTypeAdherent(TypeAdherent typeAdherent);
    Optional<Adherent> findByNomAndPrenom(String nom, String prenom);
    List<Adherent> findByDtnBefore(LocalDate date);
    boolean checkMotDePasse(Long id, String motDePasse);
}
