package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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
    private LocalDate datePenalite;
    
    @Column(length = 50)  // Modifier la longueur pour correspondre à la base de données
    private String description;
    
    @Column(name = "nb_jours_retard", nullable = false)
    private Integer nbJoursRetard;
    
    @Column(name = "date_fin_penalite", nullable = false)
    private LocalDate dateFinPenalite;
    
    @Column(name = "date_debut_penalite", nullable = false)
    private LocalDate dateDebutPenalite;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "duree_jours", nullable = false)
    private Integer dureeJours;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation = LocalDate.now(); // Valeur par défaut

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

    public LocalDate getDatePenalite() {
        return datePenalite;
    }

    public void setDatePenalite(LocalDate datePenalite) {
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

    public LocalDate getDateFinPenalite() {
        return dateFinPenalite;
    }

    public void setDateFinPenalite(LocalDate dateFinPenalite) {
        this.dateFinPenalite = dateFinPenalite;
    }

    public LocalDate getDateDebutPenalite() {
        return dateDebutPenalite;
    }

    public void setDateDebutPenalite(LocalDate dateDebutPenalite) {
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

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
}
