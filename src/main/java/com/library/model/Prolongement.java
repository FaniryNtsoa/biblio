package com.library.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prolongement")
public class Prolongement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pret_id", nullable = false)
    private Pret pret;
    
    @Column(name = "nb_jour", nullable = false)
    private Integer nbJour;
    
    @OneToMany(mappedBy = "prolongement")
    private Set<HistoriqueProlongement> historiqueProlongements = new HashSet<>();

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

    public Integer getNbJour() {
        return nbJour;
    }

    public void setNbJour(Integer nbJour) {
        this.nbJour = nbJour;
    }

    public Set<HistoriqueProlongement> getHistoriqueProlongements() {
        return historiqueProlongements;
    }

    public void setHistoriqueProlongements(Set<HistoriqueProlongement> historiqueProlongements) {
        this.historiqueProlongements = historiqueProlongements;
    }
}
