package com.library.repository;

import com.library.model.Prolongement;
import com.library.model.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {
    List<Prolongement> findByPret(Pret pret);
    long countByPret(Pret pret);
}
