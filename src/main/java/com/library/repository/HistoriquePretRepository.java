package com.library.repository;

import com.library.model.HistoriquePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriquePretRepository extends JpaRepository<HistoriquePret, Long> {

}
