package com.library.service;

import com.library.model.TypePret;
import java.util.List;
import java.util.Optional;

public interface TypePretService {
    List<TypePret> getAllTypesPret();
    Optional<TypePret> getTypePretById(Long id);
    TypePret saveTypePret(TypePret typePret);
    void deleteTypePret(Long id);
}
