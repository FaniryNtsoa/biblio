package com.library.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "type_adherent")
public class TypeAdherent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String nom;
    
    @OneToMany(mappedBy = "typeAdherent")
    private Set<Adherent> adherents = new HashSet<>();
    
    @OneToMany(mappedBy = "typeAdherent")
    private Set<GestionAdherent> gestionAdherents = new HashSet<>();

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

    public Set<Adherent> getAdherents() {
        return adherents;
    }

    public void setAdherents(Set<Adherent> adherents) {
        this.adherents = adherents;
    }

    public Set<GestionAdherent> getGestionAdherents() {
        return gestionAdherents;
    }

    public void setGestionAdherents(Set<GestionAdherent> gestionAdherents) {
        this.gestionAdherents = gestionAdherents;
    }
}
