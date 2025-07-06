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
        if (nom == null) {
            return Optional.empty();
        }
        
        // Vérifier si statuts de réservation existent déjà
        List<StatusReservation> statuts = statusReservationRepository.findAll();
        if (statuts.isEmpty()) {
            // Créer les statuts par défaut
            StatusReservation enAttente = new StatusReservation();
            enAttente.setNom("EN_ATTENTE");
            statusReservationRepository.save(enAttente);
            
            StatusReservation confirmee = new StatusReservation();
            confirmee.setNom("CONFIRMEE");
            statusReservationRepository.save(confirmee);
            
            StatusReservation annulee = new StatusReservation();
            annulee.setNom("ANNULEE");
            statusReservationRepository.save(annulee);
            
            StatusReservation expiree = new StatusReservation();
            expiree.setNom("EXPIREE");
            statusReservationRepository.save(expiree);
            
            StatusReservation rejetee = new StatusReservation();
            rejetee.setNom("REJETEE");
            statusReservationRepository.save(rejetee);
            
            // Récupérer tous les statuts après création
            statuts = statusReservationRepository.findAll();
        }
        
        return statuts.stream()
                .filter(status -> nom.equals(status.getNom()))
                .findFirst();
    }
}
