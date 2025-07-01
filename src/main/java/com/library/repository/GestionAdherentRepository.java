package com.library.repository;

import com.library.model.GestionAdherent;
import com.library.model.TypeAdherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GestionAdherentRepository extends JpaRepository<GestionAdherent, Long> {
    List<GestionAdherent> findByTypeAdherent(TypeAdherent typeAdherent);
    Optional<GestionAdherent> findByTypeAdherentId(Long typeAdherentId);
}
