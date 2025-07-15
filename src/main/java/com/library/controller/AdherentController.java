package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Inscription;
import com.library.model.Penalite;
import com.library.model.TypeAdherent;
import com.library.service.AdherentService;
import com.library.service.TypeAdherentService;
import com.library.service.InscriptionService;
import com.library.service.PretService;
import com.library.service.PenaliteService;
import com.library.service.GestionAdherentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AdherentController {

    private final AdherentService adherentService;
    private final TypeAdherentService typeAdherentService;
    private final InscriptionService inscriptionService;
    private final PretService pretService;
    private final PenaliteService penaliteService;
    private final GestionAdherentService gestionAdherentService;

    @Autowired
    public AdherentController(AdherentService adherentService,
            TypeAdherentService typeAdherentService,
            InscriptionService inscriptionService,
            PretService pretService,
            PenaliteService penaliteService,
            GestionAdherentService gestionAdherentService) {
        this.adherentService = adherentService;
        this.typeAdherentService = typeAdherentService;
        this.inscriptionService = inscriptionService;
        this.pretService = pretService;
        this.penaliteService = penaliteService;
        this.gestionAdherentService = gestionAdherentService;
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

    // Creation d'une api rest pour récupérer les informations de l'adhérent et voir
    // l'état de son adhésion , la situation de ses prêts et voir s'il a des
    // pénalités
    @GetMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<?> getAdherentDetails(HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Utilisateur non connecté"));
        }

        // Obtenir les détails de l'adhérent
        return getAdherentDetailsById(adherent.getId());
    }

    @GetMapping("/api/adherent/{id}")
    @ResponseBody
    public ResponseEntity<?> getAdherentDetailsById(@PathVariable Long id) {
        // Vérifier si l'adhérent existe
        Optional<Adherent> adherentOpt = adherentService.getAdherentById(id);
        if (adherentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Adhérent non trouvé", "id", id));
        }

        Adherent adherent = adherentOpt.get();
        Map<String, Object> result = new HashMap<>();

        // Informations de base sur l'adhérent
        Map<String, Object> adherentInfo = new HashMap<>();
        adherentInfo.put("id", adherent.getId());
        adherentInfo.put("nom", adherent.getNom());
        adherentInfo.put("prenom", adherent.getPrenom());
        adherentInfo.put("typeAdherent", adherent.getTypeAdherent().getNom());
        result.put("adherent", adherentInfo);

        // État de l'abonnement
        Map<String, Object> abonnementInfo = new HashMap<>();
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        abonnementInfo.put("actif", isActiveMember);

        Optional<Inscription> latestInscription = inscriptionService.findLatestInscriptionByAdherent(adherent);
        if (latestInscription.isPresent()) {
            Inscription inscription = latestInscription.get();
            abonnementInfo.put("dateInscription", inscription.getDateInscription().toString());
            abonnementInfo.put("dateExpiration", inscription.getDateExpiration().toString());
        }
        result.put("abonnement", abonnementInfo);

        // État des prêts
        Map<String, Object> pretsInfo = new HashMap<>();
        int nombrePretMax = gestionAdherentService.getNombrePretMaxForAdherent(adherent);
        int pretsEnCours = pretService.countPretEnCoursByAdherent(adherent);
        pretsInfo.put("nombrePretMax", nombrePretMax);
        pretsInfo.put("pretsEnCours", pretsEnCours);
        pretsInfo.put("pretsRestants", nombrePretMax - pretsEnCours);
        pretsInfo.put("pretsEnRetard", pretService.countPretEnRetardByAdherent(adherent));
        result.put("prets", pretsInfo);

        // Pénalités
        Map<String, Object> penalitesInfo = new HashMap<>();
        boolean hasPenalites = penaliteService.hasActivePenalites(adherent);
        penalitesInfo.put("actives", hasPenalites);
        if (hasPenalites) {
            List<Penalite> penalitesActives = penaliteService.findActiveByAdherent(adherent);
            List<Map<String, Object>> penalitesDetails = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();

            for (Penalite penalite : penalitesActives) {
                if (penalite.getActive() &&
                        !penalite.getDateFinPenalite().isBefore(now) &&
                        !penalite.getDateDebutPenalite().isAfter(now)) {
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", penalite.getId());
                    details.put("description", penalite.getDescription());
                    details.put("datePenalite", penalite.getDatePenalite().toString());
                    details.put("dateFinPenalite", penalite.getDateFinPenalite().toString());
                    details.put("joursRetard", penalite.getNbJoursRetard());
                    details.put("livre", penalite.getPret().getExemplaire().getLivre().getTitre());
                    penalitesDetails.add(details);
                }

            }

            penalitesInfo.put("details", penalitesDetails);
            penalitesInfo.put("dateFinPenalite", penaliteService.getDateFinPenalite(adherent).toString());
        }
        result.put("penalites", penalitesInfo);

        return ResponseEntity.ok(result);
    }
}
