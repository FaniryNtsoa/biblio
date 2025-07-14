package com.library.controller;

import com.library.model.Prolongement;
import com.library.model.Reservation;
import com.library.model.StatusReservation;
import com.library.model.User;
import com.library.service.ReservationService;
import com.library.service.StatusReservationService;
import com.library.service.UserService;
import com.library.service.PretService;
import com.library.service.HistoriqueStatusReservationService;
import com.library.service.PenaliteService;
import com.library.service.ProlongementService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bibliothecaire")
public class BibliothecaireController {
    
    private final UserService userService;
    private final ReservationService reservationService;
    private final StatusReservationService statusReservationService;
    private final PretService pretService;
    private final HistoriqueStatusReservationService historiqueStatusReservationService;
    private final PenaliteService penaliteService;
    private final ProlongementService prolongementService;
    
    @Autowired
    public BibliothecaireController(UserService userService, 
                                  ReservationService reservationService,
                                  StatusReservationService statusReservationService,
                                  PretService pretService,
                                  HistoriqueStatusReservationService historiqueStatusReservationService,
                                  PenaliteService penaliteService,
                                  ProlongementService prolongementService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.statusReservationService = statusReservationService;
        this.pretService = pretService;
        this.historiqueStatusReservationService = historiqueStatusReservationService;
        this.penaliteService = penaliteService;
        this.prolongementService = prolongementService;
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "bibliothecaire/login";
    }
    
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (userService.authenticateBibliothecaire(username, password)) {
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                session.setAttribute("bibliothecaire", userOpt.get());
                return "redirect:/bibliothecaire/dashboard";
            }
        }
        
        redirectAttributes.addFlashAttribute("error", "Identifiants invalides");
        return "redirect:/bibliothecaire/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("bibliothecaire");
        return "redirect:/";
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Vérifier si la bibliothécaire est connectée
        User bibliothecaire = (User) session.getAttribute("bibliothecaire");
        if (bibliothecaire == null || !bibliothecaire.isBibliothecaire()) {
            return "redirect:/bibliothecaire/login";
        }
        
        // Récupérer uniquement les réservations en attente (pas les annulées ni expirées)
        List<Reservation> reservationsEnAttente = reservationService.findReservationsByStatus("EN_ATTENTE");
        
        // Vérifier et mettre à jour les réservations expirées
        reservationService.updateExpiredReservations();
        
        model.addAttribute("bibliothecaire", bibliothecaire);
        model.addAttribute("reservationsEnAttente", reservationsEnAttente);
        
        return "bibliothecaire/dashboard";
    }
    
    @PostMapping("/reservations/{id}/accept")
    public String acceptReservation(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        // Vérifier si la bibliothécaire est connectée
        User bibliothecaire = (User) session.getAttribute("bibliothecaire");
        if (bibliothecaire == null || !bibliothecaire.isBibliothecaire()) {
            return "redirect:/bibliothecaire/login";
        }
        
        try {
            Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
            if (reservationOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Réservation non trouvée");
                return "redirect:/bibliothecaire/dashboard";
            }
            
            Reservation reservation = reservationOpt.get();
            
            // Vérifier que la réservation est bien en attente
            if (!reservationService.isReservationEnAttente(reservation)) {
                redirectAttributes.addFlashAttribute("error", "Cette réservation n'est plus en attente");
                return "redirect:/bibliothecaire/dashboard";
            }
            
            // Vérifier si l'adhérent a des pénalités actives
            if (penaliteService.hasActivePenalites(reservation.getAdherent())) {
                LocalDateTime dateFinPenalite = penaliteService.getDateFinPenalite(reservation.getAdherent());
                
                // Si la date de réservation est avant la fin de la pénalité, rejeter automatiquement
                if (reservation.getDateReservation().isBefore(dateFinPenalite)) {
                    // Récupérer le statut REJETEE
                    Optional<StatusReservation> statusRejeteeOpt = statusReservationService.findByNom("REJETEE");
                    if (statusRejeteeOpt.isEmpty()) {
                        redirectAttributes.addFlashAttribute("error", "Statut REJETEE non trouvé");
                        return "redirect:/bibliothecaire/dashboard";
                    }
                    
                    // Mettre à jour le statut de la réservation
                    reservationService.updateStatusReservation(reservation.getId(), statusRejeteeOpt.get());
                    
                    redirectAttributes.addFlashAttribute("error", 
                        "Réservation rejetée automatiquement car l'adhérent a une pénalité active jusqu'au " + 
                        dateFinPenalite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    return "redirect:/bibliothecaire/dashboard";
                }
            }
            
            // Récupérer le statut ACCEPTEE
            Optional<StatusReservation> statusAccepteeOpt = statusReservationService.findByNom("CONFIRMEE");
            if (statusAccepteeOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Statut CONFIRMEE non trouvé");
                return "redirect:/bibliothecaire/dashboard";
            }
            
            // Mettre à jour le statut de la réservation
            reservation = reservationService.updateStatusReservation(reservation.getId(), statusAccepteeOpt.get());
            
            // Créer un prêt à partir de la réservation
            pretService.createPretFromReservation(reservation);
            
            redirectAttributes.addFlashAttribute("success", "Réservation acceptée avec succès");
            return "redirect:/bibliothecaire/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'acceptation: " + e.getMessage());
            return "redirect:/bibliothecaire/dashboard";
        }
    }
    
    @PostMapping("/reservations/{id}/reject")
    public String rejectReservation(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        // Vérifier si la bibliothécaire est connectée
        User bibliothecaire = (User) session.getAttribute("bibliothecaire");
        if (bibliothecaire == null || !bibliothecaire.isBibliothecaire()) {
            return "redirect:/bibliothecaire/login";
        }
        
        try {
            Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
            if (reservationOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Réservation non trouvée");
                return "redirect:/bibliothecaire/dashboard";
            }
            
            Reservation reservation = reservationOpt.get();
            
            // Vérifier que la réservation est bien en attente
            if (!reservationService.isReservationEnAttente(reservation)) {
                redirectAttributes.addFlashAttribute("error", "Cette réservation n'est plus en attente");
                return "redirect:/bibliothecaire/dashboard";
            }
            
            // Récupérer le statut REJETEE
            Optional<StatusReservation> statusRejeteeOpt = statusReservationService.findByNom("REJETEE");
            if (statusRejeteeOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Statut REJETEE non trouvé");
                return "redirect:/bibliothecaire/dashboard";
            }
            
            // Mettre à jour le statut de la réservation
            reservationService.updateStatusReservation(reservation.getId(), statusRejeteeOpt.get());
            
            redirectAttributes.addFlashAttribute("success", "Réservation rejetée avec succès");
            return "redirect:/bibliothecaire/dashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du rejet: " + e.getMessage());
            return "redirect:/bibliothecaire/dashboard";
        }
    }
    
    @GetMapping("/prolongements")
    public String showProlongements(HttpSession session, Model model) {
        // Vérifier si la bibliothécaire est connectée
        User bibliothecaire = (User) session.getAttribute("bibliothecaire");
        if (bibliothecaire == null || !bibliothecaire.isBibliothecaire()) {
            return "redirect:/bibliothecaire/login";
        }
        
        // Récupérer les prolongements en attente
        List<Prolongement> prolongementsEnAttente = prolongementService.findPendingProlongements();
        
        model.addAttribute("bibliothecaire", bibliothecaire);
        model.addAttribute("prolongementsEnAttente", prolongementsEnAttente);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        return "bibliothecaire/prolongements";
    }
    
    @PostMapping("/prolongements/{id}/accept")
    public String acceptProlongement(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        // Vérifier si la bibliothécaire est connectée
        User bibliothecaire = (User) session.getAttribute("bibliothecaire");
        if (bibliothecaire == null || !bibliothecaire.isBibliothecaire()) {
            return "redirect:/bibliothecaire/login";
        }
        
        try {
            prolongementService.acceptProlongement(id);
            redirectAttributes.addFlashAttribute("success", "Prolongement accepté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'acceptation: " + e.getMessage());
        }
        
        return "redirect:/bibliothecaire/prolongements";
    }
    
    @PostMapping("/prolongements/{id}/reject")
    public String rejectProlongement(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        // Vérifier si la bibliothécaire est connectée
        User bibliothecaire = (User) session.getAttribute("bibliothecaire");
        if (bibliothecaire == null || !bibliothecaire.isBibliothecaire()) {
            return "redirect:/bibliothecaire/login";
        }
        
        try {
            prolongementService.rejectProlongement(id);
            redirectAttributes.addFlashAttribute("success", "Prolongement rejeté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du rejet: " + e.getMessage());
        }
        
        return "redirect:/bibliothecaire/prolongements";
    }
}
