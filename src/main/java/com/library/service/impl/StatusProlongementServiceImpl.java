package com.library.service.impl;

import com.library.model.StatusProlongement;
import com.library.repository.StatusProlongementRepository;
import com.library.service.StatusProlongementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StatusProlongementServiceImpl implements StatusProlongementService {

    private final StatusProlongementRepository statusProlongementRepository;

    @Autowired
    public StatusProlongementServiceImpl(StatusProlongementRepository statusProlongementRepository) {
        this.statusProlongementRepository = statusProlongementRepository;
    }

    @Override
    public List<StatusProlongement> getAllStatusProlongements() {
        return statusProlongementRepository.findAll();
    }

    @Override
    public Optional<StatusProlongement> getStatusProlongementById(Long id) {
        return statusProlongementRepository.findById(id);
    }

    @Override
    public StatusProlongement saveStatusProlongement(StatusProlongement statusProlongement) {
        return statusProlongementRepository.save(statusProlongement);
    }

    @Override
    public void deleteStatusProlongement(Long id) {
        statusProlongementRepository.deleteById(id);
    }

    @Override
    public Optional<StatusProlongement> findByNom(Integer nom) {
        return statusProlongementRepository.findByNom(nom);
    }
}
