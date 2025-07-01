package com.library.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "status_prolongement")
public class StatusProlongement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer nom;
    
    @OneToMany(mappedBy = "statusProlongement")
    private Set<HistoriqueProlongement> historiqueProlongements = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNom() {
        return nom;
    }

    public void setNom(Integer nom) {
        this.nom = nom;
    }

    public Set<HistoriqueProlongement> getHistoriqueProlongements() {
        return historiqueProlongements;
    }

    public void setHistoriqueProlongements(Set<HistoriqueProlongement> historiqueProlongements) {
        this.historiqueProlongements = historiqueProlongements;
    }
}
