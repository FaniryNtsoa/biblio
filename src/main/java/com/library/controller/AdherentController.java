package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Inscription;
import com.library.model.TypeAdherent;
import com.library.service.AdherentService;
import com.library.service.TypeAdherentService;
import com.library.service.InscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdherentController {

    private final AdherentService adherentService;
    private final TypeAdherentService typeAdherentService;
    private final InscriptionService inscriptionService;

    @Autowired
    public AdherentController(AdherentService adherentService, 
                             TypeAdherentService typeAdherentService,
                             InscriptionService inscriptionService) {
        this.adherentService = adherentService;
        this.typeAdherentService = typeAdherentService;
        this.inscriptionService = inscriptionService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        // Si l'utilisateur est déjà connecté, rediriger vers la page d'accueil
        if (session.getAttribute("adherent") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String nom, @RequestParam String motDePasse, 
                        HttpSession session, RedirectAttributes redirectAttributes) {
        
        if (adherentService.authenticateAdherentByNom(nom, motDePasse)) {
            // Récupérer l'adhérent pour stocker ses informations en session
            List<Adherent> adherents = adherentService.findByNom(nom);
            Adherent adherent = adherents.stream()
                    .filter(a -> a.getMotDePasse().equals(motDePasse))
                    .findFirst()
                    .orElse(null);
            
            if (adherent != null) {
                session.setAttribute("adherent", adherent);
                session.setAttribute("adherentId", adherent.getId());
                return "redirect:/home";
            }
        }
        
        redirectAttributes.addFlashAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        List<TypeAdherent> typeAdherents = typeAdherentService.getAllTypeAdherents();
        model.addAttribute("typeAdherents", typeAdherents);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String adresse,
            @RequestParam String motDePasse,
            @RequestParam String confirmMotDePasse,
            @RequestParam String dateNaissance,
            @RequestParam Long typeAdherentId,
            RedirectAttributes redirectAttributes) {
        
        // Vérification des mots de passe
        if (!motDePasse.equals(confirmMotDePasse)) {
            redirectAttributes.addFlashAttribute("error", "Les mots de passe ne correspondent pas");
            return "redirect:/register";
        }
        
        // Création de l'adhérent
        Adherent adherent = new Adherent();
        adherent.setNom(nom);
        adherent.setPrenom(prenom);
        adherent.setAdresse(adresse);
        adherent.setMotDePasse(motDePasse);
        

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dtn = LocalDate.parse(dateNaissance, formatter);
        adherent.setDtn(dtn);
        
        // Récupération et assignation du type d'adhérent
        typeAdherentService.getTypeAdherentById(typeAdherentId).ifPresent(adherent::setTypeAdherent);
        
        // Sauvegarde de l'adhérent
        adherentService.saveAdherent(adherent);
        
        redirectAttributes.addFlashAttribute("success", "Inscription réussie. Veuillez vous connecter.");
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est un membre actif
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
        
        // Vérifier si l'utilisateur a été redirigé vers la page d'inscription
        Boolean needMembership = (Boolean) session.getAttribute("needMembership");
        if (needMembership != null && needMembership) {
            model.addAttribute("needMembership", true);
            session.removeAttribute("needMembership");
        }
        
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
