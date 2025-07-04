package com.library.service.impl;

import com.library.model.TypePret;
import com.library.repository.TypePretRepository;
import com.library.service.TypePretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypePretServiceImpl implements TypePretService {

    private final TypePretRepository typePretRepository;

    @Autowired
    public TypePretServiceImpl(TypePretRepository typePretRepository) {
        this.typePretRepository = typePretRepository;
    }

    @Override
    public Optional<TypePret> getTypePretById(Long id) {
        return typePretRepository.findById(id);
    }

    @Override
    public TypePret saveTypePret(TypePret typePret) {
        return typePretRepository.save(typePret);
    }

    @Override
    public void deleteTypePret(Long id) {
        typePretRepository.deleteById(id);
    }

    @Override
    public List<TypePret> getAllTypesPret() {
        return typePretRepository.findAll();
    }

}
