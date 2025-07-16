package com.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gestion_adherent")
public class GestionAdherent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "type_adherent_id", nullable = false)
    private TypeAdherent typeAdherent;
    
    @Column(name = "duree_pret", nullable = false)
    private Integer dureePret;
    
    @Column(name = "nombre_pret_max", nullable = false)
    private Integer nombrePretMax;
    
    @Column(name = "nombre_reservation_max", nullable = false)
    private Integer nombreReservationMax = 3; // Valeur par défaut
    
    @Column(name = "nombre_prolongement_max", nullable = false)
    private Integer nombreProlongementMax = 1; // Valeur par défaut
    
    @Column(name = "quota_penalite_jours", nullable = false)
    private Integer quotaPenaliteJours = 7; // Valeur par défaut

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeAdherent getTypeAdherent() {
        return typeAdherent;
    }

    public void setTypeAdherent(TypeAdherent typeAdherent) {
        this.typeAdherent = typeAdherent;
    }

    public Integer getDureePret() {
        return dureePret;
    }

    public void setDureePret(Integer dureePret) {
        this.dureePret = dureePret;
    }

    public Integer getNombrePretMax() {
        return nombrePretMax;
    }

    public void setNombrePretMax(Integer nombrePretMax) {
        this.nombrePretMax = nombrePretMax;
    }
    
    public Integer getNombreReservationMax() {
        return nombreReservationMax;
    }
    
    public void setNombreReservationMax(Integer nombreReservationMax) {
        this.nombreReservationMax = nombreReservationMax;
    }
    
    public Integer getNombreProlongementMax() {
        return nombreProlongementMax;
    }
    
    public void setNombreProlongementMax(Integer nombreProlongementMax) {
        this.nombreProlongementMax = nombreProlongementMax;
    }
    
    public Integer getQuotaPenaliteJours() {
        return quotaPenaliteJours;
    }
    
    public void setQuotaPenaliteJours(Integer quotaPenaliteJours) {
        this.quotaPenaliteJours = quotaPenaliteJours;
    }
}
