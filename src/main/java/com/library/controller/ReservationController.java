package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.Reservation;
import com.library.model.StatusReservation;
import com.library.service.LivreService;
import com.library.service.ReservationService;
import com.library.service.InscriptionService;
import com.library.service.StatusReservationService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final LivreService livreService;
    private final InscriptionService inscriptionService;
    private final StatusReservationService statusReservationService;

    @Autowired
    public ReservationController(ReservationService reservationService,
                               LivreService livreService,
                               InscriptionService inscriptionService,
                               StatusReservationService statusReservationService) {
        this.reservationService = reservationService;
        this.livreService = livreService;
        this.inscriptionService = inscriptionService;
        this.statusReservationService = statusReservationService;
    }

    @GetMapping
    public String listReservations(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        if (!isActiveMember) {
            session.setAttribute("needMembership", true);
            return "redirect:/inscription";
        }
        
        // Mettre à jour les réservations expirées
        reservationService.updateExpiredReservations();
        
        // Récupérer les réservations de l'adhérent
        List<Reservation> reservations = reservationService.findReservationsByAdherent(adherent);
        
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("reservations", reservations);
        model.addAttribute("today", LocalDate.now());
        
        // Grouper les réservations par statut pour l'affichage
        List<Reservation> reservationsEnAttente = reservationService.findReservationsByAdherentAndStatus(adherent, "EN_ATTENTE");
        List<Reservation> reservationsConfirmees = reservationService.findReservationsByAdherentAndStatus(adherent, "CONFIRMEE");
        List<Reservation> reservationsRejetees = reservationService.findReservationsByAdherentAndStatus(adherent, "REJETEE");
        List<Reservation> reservationsAnnulees = reservationService.findReservationsByAdherentAndStatus(adherent, "ANNULEE");
        List<Reservation> reservationsExpirees = reservationService.findReservationsByAdherentAndStatus(adherent, "EXPIREE");
        
        model.addAttribute("reservationsEnAttente", reservationsEnAttente);
        model.addAttribute("reservationsConfirmees", reservationsConfirmees);
        model.addAttribute("reservationsRejetees", reservationsRejetees);
        model.addAttribute("reservationsAnnulees", reservationsAnnulees);
        model.addAttribute("reservationsExpirees", reservationsExpirees);
        
        return "reservations/liste";
    }

    @GetMapping("/nouveau")
    public String showReservationForm(@RequestParam(value = "livreId", required = true) Long livreId,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        if (!isActiveMember) {
            session.setAttribute("needMembership", true);
            redirectAttributes.addFlashAttribute("error", "Vous devez être membre actif pour réserver des livres.");
            return "redirect:/inscription";
        }
        
        // Récupérer le livre
        Optional<Livre> livreOpt = livreService.getLivreById(livreId);
        if (livreOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Livre non trouvé.");
            return "redirect:/livres";
        }
        
        Livre livre = livreOpt.get();
        
        model.addAttribute("livre", livre);
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("minDate", LocalDate.now());
        model.addAttribute("maxDate", LocalDate.now().plusMonths(1)); // Réservation possible jusqu'à 1 mois à l'avance
        
        return "reservations/nouveau";
    }

    @PostMapping("/nouveau")
    public String createReservation(@RequestParam Long livreId,
                                  @RequestParam String dateReservation,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        if (!isActiveMember) {
            session.setAttribute("needMembership", true);
            redirectAttributes.addFlashAttribute("error", "Vous devez être membre actif pour réserver des livres.");
            return "redirect:/inscription";
        }
        
        try {
            // Convertir la date de réservation
            LocalDate dateRes = LocalDate.parse(dateReservation);
            
            // Vérifier que la date n'est pas dans le passé
            // if (dateRes.isBefore(LocalDate.now())) {
            //     redirectAttributes.addFlashAttribute("error", "La date de réservation ne peut pas être dans le passé.");
            //     return "redirect:/reservations/nouveau?livreId=" + livreId;
            // }
            
            // Vérifier que la date n'est pas trop loin dans le futur
            // if (dateRes.isAfter(LocalDate.now().plusMonths(1))) {
            //     redirectAttributes.addFlashAttribute("error", "La date de réservation ne peut pas être à plus d'un mois dans le futur.");
            //     return "redirect:/reservations/nouveau?livreId=" + livreId;
            // }
            
            // Récupérer le livre
            Optional<Livre> livreOpt = livreService.getLivreById(livreId);
            if (livreOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Livre non trouvé.");
                return "redirect:/livres";
            }
            
            Livre livre = livreOpt.get();
            
            // Vérifier si l'adhérent a déjà une réservation en attente pour ce livre
            if (reservationService.hasActiveReservationForLivre(adherent, livre)) {
                redirectAttributes.addFlashAttribute("error", "Vous avez déjà une réservation active pour ce livre.");
                return "redirect:/reservations";
            }
            
            // Créer la réservation
            Reservation reservation = reservationService.createReservation(adherent, livre, dateRes);
            
            // Formater la date pour l'affichage
            String formattedDate = dateRes.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            redirectAttributes.addFlashAttribute("success", 
                "Votre demande de réservation pour \"" + livre.getTitre() + "\" a été soumise avec succès pour le " + 
                formattedDate + ". Elle sera examinée par un bibliothécaire.");
            
            return "redirect:/reservations";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue : " + e.getMessage());
            return "redirect:/reservations/nouveau?livreId=" + livreId;
        }
    }

    @PostMapping("/{id}/annuler")
    public String cancelReservation(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        try {
            boolean success = reservationService.cancelReservation(id, adherent);
            
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Votre réservation a été annulée avec succès.");
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "Impossible d'annuler cette réservation. Elle a peut-être déjà été traitée ou annulée.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue : " + e.getMessage());
        }
        
        return "redirect:/reservations";
    }
}
