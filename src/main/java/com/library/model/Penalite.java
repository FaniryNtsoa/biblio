package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalite")
public class Penalite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pret_id", nullable = false)
    private Pret pret;
    
    @Column(name = "date_penalite", nullable = false)
    private LocalDateTime datePenalite;
    
    @Column(length = 50)  // Modifier la longueur pour correspondre à la base de données
    private String description;
    
    @Column(name = "nb_jours_retard", nullable = false)
    private Integer nbJoursRetard;
    
    @Column(name = "date_fin_penalite", nullable = false)
    private LocalDateTime dateFinPenalite;
    
    @Column(name = "date_debut_penalite", nullable = false)
    private LocalDateTime dateDebutPenalite;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "duree_jours", nullable = false)
    private Integer dureeJours;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now(); // Valeur par défaut

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public LocalDateTime getDatePenalite() {
        return datePenalite;
    }

    public void setDatePenalite(LocalDateTime datePenalite) {
        this.datePenalite = datePenalite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNbJoursRetard() {
        return nbJoursRetard;
    }

    public void setNbJoursRetard(Integer nbJoursRetard) {
        this.nbJoursRetard = nbJoursRetard;
    }

    public LocalDateTime getDateFinPenalite() {
        return dateFinPenalite;
    }

    public void setDateFinPenalite(LocalDateTime dateFinPenalite) {
        this.dateFinPenalite = dateFinPenalite;
    }

    public LocalDateTime getDateDebutPenalite() {
        return dateDebutPenalite;
    }

    public void setDateDebutPenalite(LocalDateTime dateDebutPenalite) {
        this.dateDebutPenalite = dateDebutPenalite;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(Integer dureeJours) {
        this.dureeJours = dureeJours;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
