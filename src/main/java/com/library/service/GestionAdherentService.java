package com.library.service;

import com.library.model.GestionAdherent;
import java.util.List;
import java.util.Optional;

public interface GestionAdherentService {
    List<GestionAdherent> getAllGestionAdherents();
    Optional<GestionAdherent> getGestionAdherentById(Long id);
    GestionAdherent saveGestionAdherent(GestionAdherent gestionAdherent);
    void deleteGestionAdherent(Long id);
}
