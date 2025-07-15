package com.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "exemplaire")
public class Exemplaire {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "etat_exemplaire_id", nullable = false)
    private EtatExemplaire etatExemplaire;
    
    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    @JsonIgnore
    private Livre livre;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtatExemplaire getEtatExemplaire() {
        return etatExemplaire;
    }

    public void setEtatExemplaire(EtatExemplaire etatExemplaire) {
        this.etatExemplaire = etatExemplaire;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }
}
