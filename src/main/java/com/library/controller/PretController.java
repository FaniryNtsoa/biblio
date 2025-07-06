package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.Penalite;
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
import com.library.service.PenaliteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PenaliteService penaliteService;

    @Autowired
    public PretController(PretService pretService, 
                         LivreService livreService,
                         TypePretService typePretService,
                         InscriptionService inscriptionService,
                         PretValidationService pretValidationService,
                         ExemplaireAvailabilityService exemplaireAvailabilityService,
                         HistoriquePretService historiquePretService,
                         StatusPretService statusPretService,
                         GestionAdherentService gestionAdherentService,
                         PenaliteService penaliteService) {
        this.pretService = pretService;
        this.livreService = livreService;
        this.typePretService = typePretService;
        this.inscriptionService = inscriptionService;
        this.pretValidationService = pretValidationService;
        this.exemplaireAvailabilityService = exemplaireAvailabilityService;
        this.historiquePretService = historiquePretService;
        this.statusPretService = statusPretService;
        this.gestionAdherentService = gestionAdherentService;
        this.penaliteService = penaliteService;
    }

    @GetMapping
    public String listPrets(
            @RequestParam(value = "statutFilter", required = false) String statutFilter,
            HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif (sera géré par le filtre)
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        
        // Récupérer les prêts de l'adhérent (par défaut, seulement les prêts en cours)
        List<Pret> prets;
        
        if (statutFilter == null || statutFilter.equals("en_cours")) {
            prets = pretService.findPretEnCoursByAdherent(adherent);
            model.addAttribute("currentFilter", "en_cours");
        } else if (statutFilter.equals("tous")) {
            prets = pretService.findByAdherent(adherent);
            model.addAttribute("currentFilter", "tous");
        } else if (statutFilter.equals("retournes")) {
            prets = pretService.findPretRetournesByAdherent(adherent);
            model.addAttribute("currentFilter", "retournes");
        } else if (statutFilter.equals("en_retard")) {
            prets = pretService.findPretEnRetardByAdherent(adherent);
            model.addAttribute("currentFilter", "en_retard");
        } else {
            prets = pretService.findPretEnCoursByAdherent(adherent);
            model.addAttribute("currentFilter", "en_cours");
        }
        
        // Compter les statistiques
        int pretsEnCours = pretService.countPretEnCoursByAdherent(adherent);
        int pretsEnRetard = pretService.countPretEnRetardByAdherent(adherent);
        int pretsARendreBientot = pretService.countPretARendreBientotByAdherent(adherent, 7); // Dans les 7 prochains jours
        
        model.addAttribute("prets", prets);
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("pretsEnCours", pretsEnCours);
        model.addAttribute("pretsEnRetard", pretsEnRetard);
        model.addAttribute("pretsARendreBientot", pretsARendreBientot);
        
        // Ajouter les pénalités actives
        List<Penalite> penalitesActives = penaliteService.findActiveByAdherent(adherent);
        model.addAttribute("penalitesActives", penalitesActives);
        
        // Vérifier si l'adhérent a des pénalités actives
        boolean hasPenalites = !penalitesActives.isEmpty();
        model.addAttribute("hasPenalites", hasPenalites);
        
        // Date de fin de pénalité la plus lointaine
        if (hasPenalites) {
            LocalDate dateFinPenalite = penaliteService.getDateFinPenalite(adherent);
            model.addAttribute("dateFinPenalite", dateFinPenalite);
        }
        
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
        
        // Vérifier si l'adhérent a des pénalités en cours et ajouter un avertissement
        if (penaliteService.hasActivePenalites(adherent)) {
            LocalDate dateFinPenalite = penaliteService.getDateFinPenalite(adherent);
            model.addAttribute("warning", 
                "Attention: Vous avez une pénalité en cours jusqu'au " + 
                dateFinPenalite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ". Si vous choisissez une date de prêt pendant cette période, votre demande sera refusée.");
            
            // Ajout de l'information sur la pénalité au modèle
            model.addAttribute("hasPenalites", true);
            model.addAttribute("dateFinPenalite", dateFinPenalite);
        }
        
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
            
            // Vérifier si la date de prêt tombe pendant une période de pénalité
            if (!pretValidationService.validateNoPenalitesOnDate(adherent, datePreet)) {
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

    @GetMapping("/historique")
    public String historiquePrets(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "dateDebut", required = false) String dateDebut,
            @RequestParam(value = "dateFin", required = false) String dateFin,
            @RequestParam(value = "statutFilter", required = false) String statutFilter,
            HttpSession session, Model model) {
        
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
        
        // Récupérer tous les prêts de l'adhérent
        List<Pret> prets = pretService.findByAdherent(adherent);
        
        // Debug: afficher le nombre de prêts trouvés
        System.out.println("DEBUG - Nombre de prêts trouvés pour l'adhérent: " + prets.size());
        for (Pret pret : prets) {
            System.out.println("DEBUG - Prêt ID: " + pret.getId() + ", Livre: " + pret.getExemplaire().getLivre().getTitre());
            System.out.println("DEBUG - Historiques associés: " + pret.getHistoriquePrets().size());
        }
        
        // Appliquer les filtres
        if (search != null && !search.trim().isEmpty()) {
            prets = prets.stream()
                .filter(p -> p.getExemplaire().getLivre().getTitre().toLowerCase().contains(search.toLowerCase()) ||
                           p.getExemplaire().getLivre().getAuteur().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (dateDebut != null && !dateDebut.isEmpty()) {
            LocalDate debut = LocalDate.parse(dateDebut);
            prets = prets.stream()
                .filter(p -> p.getDatePret().isEqual(debut) || p.getDatePret().isAfter(debut))
                .collect(Collectors.toList());
        }
        
        if (dateFin != null && !dateFin.isEmpty()) {
            LocalDate fin = LocalDate.parse(dateFin);
            prets = prets.stream()
                .filter(p -> p.getDatePret().isEqual(fin) || p.getDatePret().isBefore(fin))
                .collect(Collectors.toList());
        }
        
        if (statutFilter != null && !statutFilter.isEmpty()) {
            if ("EN_COURS".equals(statutFilter)) {
                prets = pretService.findPretEnCoursByAdherent(adherent);
            } else if ("RENDU".equals(statutFilter)) {
                prets = pretService.findPretRetournesByAdherent(adherent);
            } else if ("EN_RETARD".equals(statutFilter)) {
                prets = pretService.findPretEnRetardByAdherent(adherent);
            }
        }
        
        // Après filtrage
        System.out.println("DEBUG - Nombre de prêts après filtrage: " + prets.size());
        
        // Récupérer tous les historiques de prêts pour assurer le chargement des données
        for (Pret pret : prets) {
            pret.setHistoriquePrets(new HashSet<>(historiquePretService.findByPret(pret)));
        }
        
        model.addAttribute("prets", prets);
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        
        // Conserver les valeurs des filtres
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentDateDebut", dateDebut);
        model.addAttribute("currentDateFin", dateFin);
        model.addAttribute("currentStatutFilter", statutFilter);
        
        // Récupérer les statuts pour le filtre
        List<StatusPret> statuts = statusPretService.getAllStatusPrets();
        model.addAttribute("statuts", statuts);
        
        return "prets/historique";
    }

    @PostMapping("/retourner/{id}")
    public String retournerPret(@PathVariable Long id,
                              @RequestParam String dateRetour,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        try {
            // Convertir la date de retour
            LocalDate dateRetourLd = LocalDate.parse(dateRetour);
            
            // Récupérer le prêt
            Optional<Pret> pretOpt = pretService.getPretById(id);
            if (pretOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Prêt non trouvé.");
                return "redirect:/prets";
            }
            
            Pret pret = pretOpt.get();
            
            // Vérifier que le prêt appartient bien à l'adhérent
            if (!pret.getAdherent().getId().equals(adherent.getId())) {
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à retourner ce prêt.");
                return "redirect:/prets";
            }
            
            // Vérifier que le prêt est bien en cours
            if (pretService.isPretRendu(pret)) {
                redirectAttributes.addFlashAttribute("error", "Ce prêt a déjà été retourné.");
                return "redirect:/prets";
            }
            
            // Retourner le prêt
            StatusPret statusRendu = statusPretService.getAllStatusPrets().stream()
                    .filter(s -> s.getNom().equals("RENDU"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Statut RENDU non trouvé"));
            
            // Créer l'historique du retour
            historiquePretService.createHistoriquePret(
                pret.getId(), 
                statusRendu.getId(), 
                dateRetourLd
            );
            
            // Vérifier si le retour est en retard et créer une pénalité si nécessaire
            if (pret.getDateRetourPrevue() != null && dateRetourLd.isAfter(pret.getDateRetourPrevue())) {
                // Calculer les jours de retard pour debug
                long joursRetard = ChronoUnit.DAYS.between(pret.getDateRetourPrevue(), dateRetourLd);
                System.out.println("DEBUG - Retard détecté: " + joursRetard + " jour(s)");
                System.out.println("DEBUG - Date retour prévue: " + pret.getDateRetourPrevue());
                System.out.println("DEBUG - Date retour réelle: " + dateRetourLd);
                
                // Créer une pénalité pour le retard
                Penalite penalite = penaliteService.createPenaliteForRetard(pret, dateRetourLd);
                
                if (penalite != null) {
                    System.out.println("DEBUG - Pénalité créée: ID=" + penalite.getId());
                    System.out.println("DEBUG - Jours de retard: " + penalite.getNbJoursRetard());
                    System.out.println("DEBUG - Durée jours: " + penalite.getDureeJours());
                    System.out.println("DEBUG - Active: " + penalite.getActive());
                    System.out.println("DEBUG - Date fin: " + penalite.getDateFinPenalite());
                    
                    LocalDate dateFinPenalite = penalite.getDateFinPenalite();
                    
                    redirectAttributes.addFlashAttribute("warning", 
                        "Livre retourné avec " + penalite.getNbJoursRetard() + " jour(s) de retard. " +
                        "Vous ne pourrez pas emprunter de nouveaux livres jusqu'au " + 
                        dateFinPenalite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
                } else {
                    System.out.println("DEBUG - Aucune pénalité créée malgré le retard.");
                    redirectAttributes.addFlashAttribute("success", "Livre retourné avec succès, mais une erreur est survenue lors de la création de la pénalité.");
                }
            } else {
                redirectAttributes.addFlashAttribute("success", "Livre retourné avec succès !");
            }
            
            return "redirect:/prets";
            
        } catch (Exception e) {
            e.printStackTrace(); // Ajouter ceci pour le debug
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors du retour du prêt: " + e.getMessage());
            return "redirect:/prets";
        }
    }
    
    @GetMapping("/penalites")
    public String showPenalites(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        
        // Récupérer toutes les pénalités de l'adhérent
        List<Penalite> toutesLesPenalites = penaliteService.findAllByAdherent(adherent);
        
        // Récupérer les pénalités actives
        List<Penalite> penalitesActives = penaliteService.findActiveByAdherent(adherent);
        
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("toutesLesPenalites", toutesLesPenalites);
        model.addAttribute("penalitesActives", penalitesActives);
        model.addAttribute("hasPenalites", !penalitesActives.isEmpty());
        
        if (!penalitesActives.isEmpty()) {
            LocalDate dateFinPenalite = penaliteService.getDateFinPenalite(adherent);
            model.addAttribute("dateFinPenalite", dateFinPenalite);
        }
        
        return "prets/penalites";
    }
}


