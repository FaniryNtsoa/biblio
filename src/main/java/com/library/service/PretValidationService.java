package com.library.service;

import com.library.model.Adherent;
import com.library.model.Exemplaire;
import com.library.model.TypePret;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PretValidationService {
    boolean validateAge(Adherent adherent, Exemplaire exemplaire);
    boolean validatePretLimit(Adherent adherent, TypePret typePret);
    boolean validateExemplaireAvailability(Exemplaire exemplaire);
    int getMaxPretDuration(Adherent adherent);
    String getValidationMessage();
    boolean validateNoPenalites(Adherent adherent);
    boolean validateNoPenalitesOnDate(Adherent adherent, LocalDateTime datePret);
    boolean validateNoPenalitesOnDate(Adherent adherent, LocalDate datePret);
}
