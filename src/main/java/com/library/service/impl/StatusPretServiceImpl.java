package com.library.service.impl;

import com.library.model.StatusPret;
import com.library.repository.StatusPretRepository;
import com.library.service.StatusPretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StatusPretServiceImpl implements StatusPretService {

    private final StatusPretRepository statusPretRepository;

    @Autowired
    public StatusPretServiceImpl(StatusPretRepository statusPretRepository) {
        this.statusPretRepository = statusPretRepository;
    }

    @Override
    public List<StatusPret> getAllStatusPrets() {
        return statusPretRepository.findAll();
    }

    @Override
    public Optional<StatusPret> getStatusPretById(Long id) {
        return statusPretRepository.findById(id);
    }

    @Override
    public StatusPret saveStatusPret(StatusPret statusPret) {
        return statusPretRepository.save(statusPret);
    }

    @Override
    public void deleteStatusPret(Long id) {
        statusPretRepository.deleteById(id);
    }

}
