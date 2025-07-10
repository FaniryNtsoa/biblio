package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_pret")
public class HistoriquePret {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "status_pret_id", nullable = false)
    private StatusPret statusPret;
    
    @ManyToOne
    @JoinColumn(name = "pret_id", nullable = false)
    private Pret pret;
    
    @ManyToOne
    @JoinColumn(name = "exemplaire_id", nullable = false)
    private Exemplaire exemplaire;
    
    @Column(name = "date_retour")
    private LocalDateTime dateRetour;
    
    @Column(name = "date_changement", nullable = false)
    private LocalDateTime dateChangement;
    
    @Column(name = "commentaire")
    private String commentaire;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusPret getStatusPret() {
        return statusPret;
    }

    public void setStatusPret(StatusPret statusPret) {
        this.statusPret = statusPret;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public LocalDateTime getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
