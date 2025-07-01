package com.library.service;

import com.library.model.TypeAdherent;
import java.util.List;
import java.util.Optional;

public interface TypeAdherentService {
    List<TypeAdherent> getAllTypeAdherents();
    Optional<TypeAdherent> getTypeAdherentById(Long id);
    TypeAdherent saveTypeAdherent(TypeAdherent typeAdherent);
    void deleteTypeAdherent(Long id);
    Optional<TypeAdherent> findByNom(String nom);
}
