package com.library.service;

import com.library.model.Exemplaire;
import com.library.model.Livre;
import java.util.List;

public interface ExemplaireAvailabilityService {
    List<Exemplaire> getAvailableExemplaires(Livre livre);
    Exemplaire getFirstAvailableExemplaire(Livre livre);
    boolean isExemplaireAvailable(Exemplaire exemplaire);
}
