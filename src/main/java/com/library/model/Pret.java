package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pret")
public class Pret {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "adherent_id", nullable = false)
    private Adherent adherent;
    
    @ManyToOne
    @JoinColumn(name = "type_pret_id", nullable = false)
    private TypePret typePret;
    
    @Column(name = "date_pret", nullable = false)
    private LocalDate datePret;
    
    @OneToMany(mappedBy = "pret")
    private Set<Penalite> penalites = new HashSet<>();
    
    @OneToMany(mappedBy = "pret")
    private Set<HistoriquePret> historiquePrets = new HashSet<>();
    
    @OneToMany(mappedBy = "pret")
    private Set<Prolongement> prolongements = new HashSet<>();

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

    public TypePret getTypePret() {
        return typePret;
    }

    public void setTypePret(TypePret typePret) {
        this.typePret = typePret;
    }

    public LocalDate getDatePret() {
        return datePret;
    }

    public void setDatePret(LocalDate datePret) {
        this.datePret = datePret;
    }

    public Set<Penalite> getPenalites() {
        return penalites;
    }

    public void setPenalites(Set<Penalite> penalites) {
        this.penalites = penalites;
    }

    public Set<HistoriquePret> getHistoriquePrets() {
        return historiquePrets;
    }

    public void setHistoriquePrets(Set<HistoriquePret> historiquePrets) {
        this.historiquePrets = historiquePrets;
    }

    public Set<Prolongement> getProlongements() {
        return prolongements;
    }

    public void setProlongements(Set<Prolongement> prolongements) {
        this.prolongements = prolongements;
    }
}
