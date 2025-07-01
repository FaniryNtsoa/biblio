package com.library.repository;

import com.library.model.Reservation;
import com.library.model.Adherent;
import com.library.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAdherent(Adherent adherent);
    List<Reservation> findByLivre(Livre livre);
    List<Reservation> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Reservation> findByDateExpirationBefore(LocalDate date);
    Optional<Reservation> findByAdherentAndLivre(Adherent adherent, Livre livre);
}
