package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.Pret;
import com.library.model.TypePret;
import com.library.service.PretService;
import com.library.service.LivreService;
import com.library.service.TypePretService;
import com.library.service.InscriptionService;
import com.library.service.PretValidationService;
import com.library.service.ExemplaireAvailabilityService;
import com.library.service.HistoriquePretService;
import com.library.service.StatusPretService;
import com.library.model.Exemplaire;
import com.library.model.StatusPret;
import com.library.service.GestionAdherentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/prets")
public class PretController {

    private final PretService pretService;
    private final LivreService livreService;
    private final TypePretService typePretService;
    private final InscriptionService inscriptionService;
    private final PretValidationService pretValidationService;
    private final ExemplaireAvailabilityService exemplaireAvailabilityService;
    private final HistoriquePretService historiquePretService;
    private final StatusPretService statusPretService;
    private final GestionAdherentService gestionAdherentService;

    @Autowired
    public PretController(PretService pretService, 
                         LivreService livreService,
                         TypePretService typePretService,
                         InscriptionService inscriptionService,
                         PretValidationService pretValidationService,
                         ExemplaireAvailabilityService exemplaireAvailabilityService,
                         HistoriquePretService historiquePretService,
                         StatusPretService statusPretService,
                         GestionAdherentService gestionAdherentService) {
        this.pretService = pretService;
        this.livreService = livreService;
        this.typePretService = typePretService;
        this.inscriptionService = inscriptionService;
        this.pretValidationService = pretValidationService;
        this.exemplaireAvailabilityService = exemplaireAvailabilityService;
        this.historiquePretService = historiquePretService;
        this.statusPretService = statusPretService;
        this.gestionAdherentService = gestionAdherentService;
    }

    @GetMapping
    public String listPrets(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif (sera géré par le filtre)
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        
        // Récupérer les prêts de l'adhérent
        List<Pret> prets = pretService.findByAdherent(adherent);
        
        model.addAttribute("prets", prets);
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        
        return "prets/liste";
    }

    @GetMapping("/nouveau")
    public String showPretForm(@RequestParam(value = "livreId", required = false) Long livreId,
                              HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif (double vérification)
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        if (!isActiveMember) {
            session.setAttribute("needMembership", true);
            redirectAttributes.addFlashAttribute("error", "Vous devez être membre actif pour emprunter des livres.");
            return "redirect:/inscription";
        }
        
        // Récupérer les types de prêt disponibles
        List<TypePret> typesPret = typePretService.getAllTypesPret();
        if (typesPret.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Aucun type de prêt disponible. Contactez l'administrateur.");
            return "redirect:/livres";
        }
        
        model.addAttribute("typesPret", typesPret);
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        
        // DEBUG: Vérifier les règles de gestion
        int dureePretMax = gestionAdherentService.getDureePretForAdherent(adherent);
        int nombrePretMax = gestionAdherentService.getNombrePretMaxForAdherent(adherent);
        
        System.out.println("=== DEBUG RÈGLES DE GESTION ===");
        System.out.println("Adhérent: " + adherent.getPrenom() + " " + adherent.getNom());
        System.out.println("Type: " + adherent.getTypeAdherent().getNom());
        System.out.println("Durée max: " + dureePretMax + " jours");
        System.out.println("Nombre max: " + nombrePretMax + " prêts");
        
        // Vérifier si la table gestion_adherent a des données
        List<com.library.model.GestionAdherent> regles = gestionAdherentService.getAllGestionAdherents();
        System.out.println("Nombre de règles dans la base: " + regles.size());
        for (com.library.model.GestionAdherent regle : regles) {
            System.out.println("- Type: " + regle.getTypeAdherent().getNom() + 
                             ", Durée: " + regle.getDureePret() + 
                             ", Max: " + regle.getNombrePretMax());
        }
        System.out.println("=== FIN DEBUG ===");
        
        model.addAttribute("dureePretMax", dureePretMax);
        model.addAttribute("nombrePretMax", nombrePretMax);
        
        // Si un livre spécifique est sélectionné
        if (livreId != null) {
            Optional<Livre> livre = livreService.getLivreById(livreId);
            if (livre.isPresent()) {
                // Vérifier que le livre est disponible
                if (livre.get().getExemplaires().isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Ce livre n'est pas disponible pour le moment.");
                    return "redirect:/livres";
                }
                model.addAttribute("selectedLivre", livre.get());
            } else {
                redirectAttributes.addFlashAttribute("error", "Livre non trouvé.");
                return "redirect:/livres";
            }
        }
        
        // Récupérer tous les livres disponibles (avec au moins un exemplaire disponible)
        List<Livre> livresDisponibles = livreService.getAllLivres().stream()
                .filter(livre -> !exemplaireAvailabilityService.getAvailableExemplaires(livre).isEmpty())
                .toList();
        
        if (livresDisponibles.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Aucun livre n'est disponible pour le moment.");
            return "redirect:/livres";
        }
        
        model.addAttribute("livresDisponibles", livresDisponibles);
        
        return "prets/nouveau";
    }

    @PostMapping("/nouveau")
    public String createPret(@RequestParam Long livreId,
                            @RequestParam Long typePretId,
                            @RequestParam String datePret,
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
            redirectAttributes.addFlashAttribute("error", "Votre adhésion a expiré. Veuillez renouveler votre adhésion.");
            return "redirect:/inscription";
        }
        
        try {
            // Convertir la date de prêt
            LocalDate datePreet = LocalDate.parse(datePret);
            
            // Vérifier que la date n'est pas dans le passé
            // if (datePreet.isBefore(LocalDate.now())) {
            //     redirectAttributes.addFlashAttribute("error", "La date de prêt ne peut pas être dans le passé.");
            //     return "redirect:/prets/nouveau?livreId=" + livreId;
            // }
            
            // Vérifier que le livre existe
            Optional<Livre> livre = livreService.getLivreById(livreId);
            if (livre.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Livre non trouvé.");
                return "redirect:/prets/nouveau";
            }
            
            // Vérifier que le type de prêt existe
            Optional<TypePret> typePret = typePretService.getTypePretById(typePretId);
            if (typePret.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Type de prêt non valide.");
                return "redirect:/prets/nouveau";
            }
            
            // Récupérer un exemplaire disponible
            Exemplaire exemplaire = exemplaireAvailabilityService.getFirstAvailableExemplaire(livre.get());
            if (exemplaire == null) {
                redirectAttributes.addFlashAttribute("error", "Aucun exemplaire disponible pour ce livre.");
                return "redirect:/prets/nouveau?livreId=" + livreId;
            }
            
            // Validation de l'âge
            if (!pretValidationService.validateAge(adherent, exemplaire)) {
                redirectAttributes.addFlashAttribute("error", pretValidationService.getValidationMessage());
                return "redirect:/prets/nouveau?livreId=" + livreId;
            }
            
            // Validation de la limite de prêts
            if (!pretValidationService.validatePretLimit(adherent, typePret.get())) {
                redirectAttributes.addFlashAttribute("error", pretValidationService.getValidationMessage());
                return "redirect:/prets/nouveau?livreId=" + livreId;
            }
            
            // Validation de la disponibilité de l'exemplaire
            if (!pretValidationService.validateExemplaireAvailability(exemplaire)) {
                redirectAttributes.addFlashAttribute("error", pretValidationService.getValidationMessage());
                return "redirect:/prets/nouveau?livreId=" + livreId;
            }
            
            // Calculer la date de retour prévue
            LocalDate dateRetourPrevue = null;
            if (!typePret.get().getSurPlace()) {
                int dureePret = gestionAdherentService.getDureePretForAdherent(adherent);
                dateRetourPrevue = datePreet.plusDays(dureePret);
            }
            
            // Créer le prêt
            Pret pret = new Pret();
            pret.setAdherent(adherent);
            pret.setTypePret(typePret.get());
            pret.setExemplaire(exemplaire);
            pret.setDatePret(datePreet);
            pret.setDateRetourPrevue(dateRetourPrevue);
            
            Pret savedPret = pretService.savePret(pret);
            
            // Déterminer le statut initial
            String statusNom = typePret.get().getSurPlace() ? "RENDU" : "EN_COURS";
            StatusPret statusPret = statusPretService.getAllStatusPrets().stream()
                    .filter(s -> s.getNom().equals(statusNom))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Statut " + statusNom + " non trouvé"));
            
            // Créer l'historique du prêt
            historiquePretService.createHistoriquePret(
                savedPret.getId(), 
                statusPret.getId(), 
                typePret.get().getSurPlace() ? datePreet : null
            );
            
            String message = typePret.get().getSurPlace() 
                ? "Consultation sur place de \"" + livre.get().getTitre() + "\" enregistrée avec succès !"
                : "Votre prêt de \"" + livre.get().getTitre() + "\" a été enregistré avec succès ! À rendre le " + 
                  dateRetourPrevue.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            redirectAttributes.addFlashAttribute("success", message);
            
            return "redirect:/prets";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la création du prêt: " + e.getMessage());
            return "redirect:/prets/nouveau" + (livreId != null ? "?livreId=" + livreId : "");
        }
    }
}
