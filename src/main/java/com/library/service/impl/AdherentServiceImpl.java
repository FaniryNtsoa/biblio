package com.library.service.impl;

import com.library.model.Adherent;
import com.library.model.TypeAdherent;
import com.library.repository.AdherentRepository;
import com.library.service.AdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        return adherentRepository.findByNomContainingIgnoreCase(nom);
    }

    @Override
    public List<Adherent> findByPrenom(String prenom) {
        return adherentRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    @Override
    public List<Adherent> findByTypeAdherent(TypeAdherent typeAdherent) {
        return adherentRepository.findByTypeAdherent(typeAdherent);
    }

    @Override
    public Optional<Adherent> findByNomAndPrenom(String nom, String prenom) {
        return adherentRepository.findByNomAndPrenom(nom, prenom);
    }

    @Override
    public List<Adherent> findByDtnBefore(LocalDate date) {
        return adherentRepository.findByDtnBefore(date);
    }

    @Override
    public boolean checkMotDePasse(Long id, String motDePasse) {
        Optional<Adherent> adherent = adherentRepository.findById(id);
        return adherent.map(a -> a.getMotDePasse().equals(motDePasse)).orElse(false);
    }
}
