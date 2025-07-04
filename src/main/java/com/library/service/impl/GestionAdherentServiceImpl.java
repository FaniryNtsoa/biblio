package com.library.service.impl;

import com.library.model.GestionAdherent;
import com.library.model.TypeAdherent;
import com.library.model.Adherent;
import com.library.repository.GestionAdherentRepository;
import com.library.service.GestionAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionAdherentServiceImpl implements GestionAdherentService {

    private final GestionAdherentRepository gestionAdherentRepository;

    @Autowired
    public GestionAdherentServiceImpl(GestionAdherentRepository gestionAdherentRepository) {
        this.gestionAdherentRepository = gestionAdherentRepository;
    }

    @Override
    public List<GestionAdherent> getAllGestionAdherents() {
        return gestionAdherentRepository.findAll();
    }

    @Override
    public Optional<GestionAdherent> getGestionAdherentById(Long id) {
        return gestionAdherentRepository.findById(id);
    }

    @Override
    public GestionAdherent saveGestionAdherent(GestionAdherent gestionAdherent) {
        return gestionAdherentRepository.save(gestionAdherent);
    }

    @Override
    public void deleteGestionAdherent(Long id) {
        gestionAdherentRepository.deleteById(id);
    }

    @Override
    public Optional<GestionAdherent> findByTypeAdherent(TypeAdherent typeAdherent) {
        return gestionAdherentRepository.findAll().stream()
                .filter(g -> g.getTypeAdherent().getId().equals(typeAdherent.getId()))
                .findFirst();
    }

    @Override
    public int getDureePretForAdherent(Adherent adherent) {
        Optional<GestionAdherent> gestion = findByTypeAdherent(adherent.getTypeAdherent());
        if (gestion.isPresent()) {
            System.out.println("Règle trouvée pour " + adherent.getTypeAdherent().getNom() +
                    ": " + gestion.get().getDureePret() + " jours");
            return gestion.get().getDureePret();
        } else {
            System.out.println("Aucune règle trouvée pour " + adherent.getTypeAdherent().getNom() +
                    ", utilisation de la valeur par défaut: 14 jours");
            return 14; // Valeur par défaut
        }
    }

    @Override
    public int getNombrePretMaxForAdherent(Adherent adherent) {
        Optional<GestionAdherent> gestion = findByTypeAdherent(adherent.getTypeAdherent());
        if (gestion.isPresent()) {
            System.out.println("Règle trouvée pour " + adherent.getTypeAdherent().getNom() +
                    ": " + gestion.get().getNombrePretMax() + " prêts max");
            return gestion.get().getNombrePretMax();
        } else {
            System.out.println("Aucune règle trouvée pour " + adherent.getTypeAdherent().getNom() +
                    ", utilisation de la valeur par défaut: 2 prêts");
            return 2; // Valeur par défaut
        }
    }

    @Override
    public boolean canAdherentBorrow(Adherent adherent, int currentPretCount) {
        int maxPrets = getNombrePretMaxForAdherent(adherent);
        return currentPretCount < maxPrets;
    }
}
