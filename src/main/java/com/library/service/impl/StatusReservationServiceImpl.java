package com.library.service.impl;

import com.library.model.StatusReservation;
import com.library.repository.StatusReservationRepository;
import com.library.service.StatusReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StatusReservationServiceImpl implements StatusReservationService {

    private final StatusReservationRepository statusReservationRepository;

    @Autowired
    public StatusReservationServiceImpl(StatusReservationRepository statusReservationRepository) {
        this.statusReservationRepository = statusReservationRepository;
    }

    @Override
    public List<StatusReservation> getAllStatusReservations() {
        return statusReservationRepository.findAll();
    }

    @Override
    public Optional<StatusReservation> getStatusReservationById(Long id) {
        return statusReservationRepository.findById(id);
    }

    @Override
    public StatusReservation saveStatusReservation(StatusReservation statusReservation) {
        return statusReservationRepository.save(statusReservation);
    }

    @Override
    public void deleteStatusReservation(Long id) {
        statusReservationRepository.deleteById(id);
    }

    @Override
    public Optional<StatusReservation> findByNom(String nom) {
        return statusReservationRepository.findByNom(nom);
    }
}
