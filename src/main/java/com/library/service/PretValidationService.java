package com.library.service;

import com.library.model.Adherent;
import com.library.model.Exemplaire;
import com.library.model.TypePret;
import java.time.LocalDate;

public interface PretValidationService {
    boolean validateAge(Adherent adherent, Exemplaire exemplaire);
    boolean validatePretLimit(Adherent adherent, TypePret typePret);
    boolean validateExemplaireAvailability(Exemplaire exemplaire);
    boolean validateNoPenalites(Adherent adherent);
    boolean validateNoPenalitesOnDate(Adherent adherent, LocalDate datePret);
    int getMaxPretDuration(Adherent adherent);
    String getValidationMessage();
}
