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
}
