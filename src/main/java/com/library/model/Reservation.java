package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;
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
    private LocalDate dateReservation;
    
    @Column(name = "date_expiration")
    private LocalDate dateExpiration;
    
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

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Set<HistoriqueStatusReservation> getHistoriqueStatusReservations() {
        return historiqueStatusReservations;
    }

    public void setHistoriqueStatusReservations(Set<HistoriqueStatusReservation> historiqueStatusReservations) {
        this.historiqueStatusReservations = historiqueStatusReservations;
    }
}
