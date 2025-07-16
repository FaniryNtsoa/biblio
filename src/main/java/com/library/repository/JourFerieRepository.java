package com.library.repository;

import com.library.model.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {
    List<JourFerie> findByDateBetween(LocalDate debut, LocalDate fin);
    boolean existsByDate(LocalDate date);
}
