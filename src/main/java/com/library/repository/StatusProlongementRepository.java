package com.library.repository;

import com.library.model.StatusProlongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusProlongementRepository extends JpaRepository<StatusProlongement, Long> {
    Optional<StatusProlongement> findByNom(Integer nom);
}
