package com.library.repository;

import com.library.model.HistoriqueProlongement;
import com.library.model.Prolongement;
import com.library.model.StatusProlongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueProlongementRepository extends JpaRepository<HistoriqueProlongement, Long> {
    List<HistoriqueProlongement> findByProlongement(Prolongement prolongement);
    List<HistoriqueProlongement> findByStatusProlongement(StatusProlongement statusProlongement);
}
