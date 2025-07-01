package com.library.service;

import com.library.model.GestionAdherent;
import com.library.model.TypeAdherent;
import java.util.List;
import java.util.Optional;

public interface GestionAdherentService {
    List<GestionAdherent> getAllGestionAdherents();
    Optional<GestionAdherent> getGestionAdherentById(Long id);
    GestionAdherent saveGestionAdherent(GestionAdherent gestionAdherent);
    void deleteGestionAdherent(Long id);
    List<GestionAdherent> findByTypeAdherent(TypeAdherent typeAdherent);
    Optional<GestionAdherent> findByTypeAdherentId(Long typeAdherentId);
    Integer getDureePretByTypeAdherent(Long typeAdherentId);
    Integer getNombrePretMaxByTypeAdherent(Long typeAdherentId);
}
