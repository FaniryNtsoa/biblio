package com.library.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "status_reservation")
public class StatusReservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String nom;
    
    @OneToMany(mappedBy = "statusReservation")
    private Set<HistoriqueStatusReservation> historiqueStatusReservations = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<HistoriqueStatusReservation> getHistoriqueStatusReservations() {
        return historiqueStatusReservations;
    }

    public void setHistoriqueStatusReservations(Set<HistoriqueStatusReservation> historiqueStatusReservations) {
        this.historiqueStatusReservations = historiqueStatusReservations;
    }
}
