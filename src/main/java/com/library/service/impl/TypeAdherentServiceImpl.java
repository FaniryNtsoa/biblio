package com.library.service.impl;

import com.library.model.TypeAdherent;
import com.library.repository.TypeAdherentRepository;
import com.library.service.TypeAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypeAdherentServiceImpl implements TypeAdherentService {

    private final TypeAdherentRepository typeAdherentRepository;

    @Autowired
    public TypeAdherentServiceImpl(TypeAdherentRepository typeAdherentRepository) {
        this.typeAdherentRepository = typeAdherentRepository;
    }

    @Override
    public List<TypeAdherent> getAllTypeAdherents() {
        return typeAdherentRepository.findAll();
    }

    @Override
    public Optional<TypeAdherent> getTypeAdherentById(Long id) {
        return typeAdherentRepository.findById(id);
    }

    @Override
    public TypeAdherent saveTypeAdherent(TypeAdherent typeAdherent) {
        return typeAdherentRepository.save(typeAdherent);
    }

    @Override
    public void deleteTypeAdherent(Long id) {
        typeAdherentRepository.deleteById(id);
    }

}
