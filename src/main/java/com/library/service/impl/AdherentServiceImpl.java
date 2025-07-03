package com.library.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.model.Adherent;
import com.library.model.TypeAdherent;
import com.library.repository.AdherentRepository;
import com.library.service.AdherentService;

@Service
public class AdherentServiceImpl implements AdherentService {

    private final AdherentRepository adherentRepository;

    @Autowired
    public AdherentServiceImpl(AdherentRepository adherentRepository) {
        this.adherentRepository = adherentRepository;
    }

    @Override
    public List<Adherent> getAllAdherents() {
        return adherentRepository.findAll();
    }

    @Override
    public Optional<Adherent> getAdherentById(Long id) {
        return adherentRepository.findById(id);
    }

    @Override
    public Adherent saveAdherent(Adherent adherent) {
        return adherentRepository.save(adherent);
    }

    @Override
    public void deleteAdherent(Long id) {
        adherentRepository.deleteById(id);
    }

    @Override
    public List<Adherent> findByNom(String nom) {
        return adherentRepository.findAll().stream()
                .filter(adherent -> adherent.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Adherent> findByPrenom(String prenom) {
        return adherentRepository.findAll().stream()
                .filter(adherent -> adherent.getPrenom().toLowerCase().contains(prenom.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Adherent> findByTypeAdherent(TypeAdherent typeAdherent) {
        return adherentRepository.findAll().stream()
                .filter(adherent -> adherent.getTypeAdherent().equals(typeAdherent))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Adherent> findByNomAndPrenom(String nom, String prenom) {
        return adherentRepository.findAll().stream()
                .filter(adherent -> adherent.getNom().equalsIgnoreCase(nom) && 
                                   adherent.getPrenom().equalsIgnoreCase(prenom))
                .findFirst();
    }

    @Override
    public List<Adherent> findByDtnBefore(LocalDate date) {
        return adherentRepository.findAll().stream()
                .filter(adherent -> adherent.getDtn().isBefore(date))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkMotDePasse(Long id, String motDePasse) {
        Optional<Adherent> adherent = getAdherentById(id);
        return adherent.map(a -> a.getMotDePasse().equals(motDePasse)).orElse(false);
    }

    @Override
    public boolean authenticateAdherent(String nom, String prenom, String motDePasse) {
        Optional<Adherent> adherent = findByNomAndPrenom(nom, prenom);
        return adherent.map(a -> a.getMotDePasse().equals(motDePasse)).orElse(false);
    }

    @Override
    public boolean authenticateAdherentByNom(String nom, String motDePasse) {
        List<Adherent> adherents = findByNom(nom);
        return adherents.stream()
                .anyMatch(a -> a.getMotDePasse().equals(motDePasse));
    }
}
