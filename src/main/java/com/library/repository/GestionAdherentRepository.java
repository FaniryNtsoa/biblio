package com.library.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.library.model.GestionAdherent;

@Repository
public interface GestionAdherentRepository extends JpaRepository<GestionAdherent, Long> {

}