package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "adherent_id", nullable = false)
    private Adherent adherent;
    
    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;
    
    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation;
    
    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;
    
    @OneToMany(mappedBy = "reservation")
    private Set<HistoriqueStatusReservation> historiqueStatusReservations = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Set<HistoriqueStatusReservation> getHistoriqueStatusReservations() {
        return historiqueStatusReservations;
    }

    public void setHistoriqueStatusReservations(Set<HistoriqueStatusReservation> historiqueStatusReservations) {
        this.historiqueStatusReservations = historiqueStatusReservations;
    }
}
