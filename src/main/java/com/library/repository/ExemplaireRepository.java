package com.library.repository;

import com.library.model.Exemplaire;
import com.library.model.Livre;
import com.library.model.EtatExemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    List<Exemplaire> findByLivre(Livre livre);
    List<Exemplaire> findByEtatExemplaire(EtatExemplaire etatExemplaire);
    long countByLivre(Livre livre);
}
