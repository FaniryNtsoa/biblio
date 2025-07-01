package com.library.service;

import com.library.model.StatusProlongement;
import java.util.List;
import java.util.Optional;

public interface StatusProlongementService {
    List<StatusProlongement> getAllStatusProlongements();
    Optional<StatusProlongement> getStatusProlongementById(Long id);
    StatusProlongement saveStatusProlongement(StatusProlongement statusProlongement);
    void deleteStatusProlongement(Long id);
    Optional<StatusProlongement> findByNom(Integer nom);
}
