package com.library.service;

import com.library.model.StatusPret;
import java.util.List;
import java.util.Optional;

public interface StatusPretService {
    List<StatusPret> getAllStatusPrets();
    Optional<StatusPret> getStatusPretById(Long id);
    StatusPret saveStatusPret(StatusPret statusPret);
    void deleteStatusPret(Long id);
    Optional<StatusPret> findByNom(String nom);
}
