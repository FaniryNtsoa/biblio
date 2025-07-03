package com.library.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.library.model.GestionAdherent;
import com.library.repository.GestionAdherentRepository;
import com.library.service.GestionAdherentService;

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

}
