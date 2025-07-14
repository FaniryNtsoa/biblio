package com.library.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "historique_prolongement")
public class HistoriqueProlongement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "status_prolongement_id", nullable = false)
    private StatusProlongement statusProlongement;
    
    @ManyToOne
    @JoinColumn(name = "prolongement_id", nullable = false)
    private Prolongement prolongement;

    @Column(name = "date_changement", nullable = false)
    private LocalDateTime dateChangement;

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusProlongement getStatusProlongement() {
        return statusProlongement;
    }

    public void setStatusProlongement(StatusProlongement statusProlongement) {
        this.statusProlongement = statusProlongement;
    }

    public Prolongement getProlongement() {
        return prolongement;
    }

    public void setProlongement(Prolongement prolongement) {
        this.prolongement = prolongement;
    }
}
