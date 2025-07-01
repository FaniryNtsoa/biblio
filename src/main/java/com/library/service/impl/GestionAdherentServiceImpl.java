package com.library.service.impl;

import com.library.model.GestionAdherent;
import com.library.model.TypeAdherent;
import com.library.repository.GestionAdherentRepository;
import com.library.service.GestionAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GestionAdherentServiceImpl implements GestionAdherentService {

    private final GestionAdherentRepository gestionAdherentRepository;

    @Autowired
    public GestionAdherentServiceImpl(GestionAdherentRepository gestionAdherentRepository) {
        this.gestionAdherentRepository = gestionAdherentRepository;
    }

    @Override
    public List<GestionAdherent> getAllGestionAdherents() {
        return gestionAdherentRepository.findAll();
    }

    @Override
    public Optional<GestionAdherent> getGestionAdherentById(Long id) {
        return gestionAdherentRepository.findById(id);
    }

    @Override
    public GestionAdherent saveGestionAdherent(GestionAdherent gestionAdherent) {
        return gestionAdherentRepository.save(gestionAdherent);
    }

    @Override
    public void deleteGestionAdherent(Long id) {
        gestionAdherentRepository.deleteById(id);
    }

    @Override
    public List<GestionAdherent> findByTypeAdherent(TypeAdherent typeAdherent) {
        return gestionAdherentRepository.findByTypeAdherent(typeAdherent);
    }

    @Override
    public Optional<GestionAdherent> findByTypeAdherentId(Long typeAdherentId) {
        return gestionAdherentRepository.findByTypeAdherentId(typeAdherentId);
    }

    @Override
    public Integer getDureePretByTypeAdherent(Long typeAdherentId) {
        Optional<GestionAdherent> gestionAdherent = gestionAdherentRepository.findByTypeAdherentId(typeAdherentId);
        return gestionAdherent.map(GestionAdherent::getDureePret).orElse(null);
    }

    @Override
    public Integer getNombrePretMaxByTypeAdherent(Long typeAdherentId) {
        Optional<GestionAdherent> gestionAdherent = gestionAdherentRepository.findByTypeAdherentId(typeAdherentId);
        return gestionAdherent.map(GestionAdherent::getNombrePretMax).orElse(null);
    }
}
