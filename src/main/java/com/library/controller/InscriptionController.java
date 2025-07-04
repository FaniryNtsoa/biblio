package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Inscription;
import com.library.service.AdherentService;
import com.library.service.InscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/inscription")
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final AdherentService adherentService;

    @Autowired
    public InscriptionController(InscriptionService inscriptionService, AdherentService adherentService) {
        this.inscriptionService = inscriptionService;
        this.adherentService = adherentService;
    }

    @GetMapping
    public String showInscriptionForm(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est déjà membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        
        // Récupérer la dernière inscription si elle existe
        Optional<Inscription> latestInscription = inscriptionService.findLatestInscriptionByAdherent(adherent);
        
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        
        // Formater les dates et les passer au modèle
        Map<String, String> formattedDates = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        if (latestInscription.isPresent()) {
            Inscription inscription = latestInscription.get();
            formattedDates.put("dateInscription", inscription.getDateInscription().format(formatter));
            formattedDates.put("dateExpiration", inscription.getDateExpiration().format(formatter));
            model.addAttribute("latestInscription", inscription);
        }
        
        model.addAttribute("formattedDates", formattedDates);
        
        return "inscription/formulaire";
    }

    @PostMapping
    public String processInscription(
            @RequestParam("dateExpiration") String dateExpirationStr,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        try {
            // Convertir la date d'expiration de chaîne à LocalDate
            LocalDate dateExpiration = LocalDate.parse(dateExpirationStr);
            
            // Vérifier que la date d'expiration est dans le futur
            if (dateExpiration.isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "La date d'expiration doit être dans le futur.");
                return "redirect:/inscription";
            }
            
            // Créer une nouvelle inscription
            Inscription inscription = inscriptionService.createNewInscription(adherent, dateExpiration);
            
            redirectAttributes.addFlashAttribute("success", 
                "Félicitations ! Votre inscription est valide jusqu'au " + 
                inscription.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de l'inscription: " + e.getMessage());
            return "redirect:/inscription";
        }
    }

    @GetMapping("/status")
    public String checkMembershipStatus(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        Optional<Inscription> latestInscription = inscriptionService.findLatestInscriptionByAdherent(adherent);
        
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        
        // Formater les dates et les passer au modèle
        Map<String, String> formattedDates = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        if (latestInscription.isPresent()) {
            Inscription inscription = latestInscription.get();
            formattedDates.put("dateInscription", inscription.getDateInscription().format(formatter));
            formattedDates.put("dateExpiration", inscription.getDateExpiration().format(formatter));
            model.addAttribute("latestInscription", inscription);
        }
        
        model.addAttribute("formattedDates", formattedDates);
        
        return "inscription/status";
    }
}