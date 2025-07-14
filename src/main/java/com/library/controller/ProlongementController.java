package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Pret;
import com.library.model.Prolongement;
import com.library.service.PretService;
import com.library.service.ProlongementService;
import com.library.service.InscriptionService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/prolongements")
public class ProlongementController {

    private final ProlongementService prolongementService;
    private final PretService pretService;
    private final InscriptionService inscriptionService;

    @Autowired
    public ProlongementController(ProlongementService prolongementService, 
                                 PretService pretService,
                                 InscriptionService inscriptionService) {
        this.prolongementService = prolongementService;
        this.pretService = pretService;
        this.inscriptionService = inscriptionService;
    }
    
    @GetMapping
    public String listProlongements(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est un membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        if (!isActiveMember) {
            session.setAttribute("needMembership", true);
            return "redirect:/inscription";
        }
        
        // Récupérer les prêts en cours de l'adhérent
        List<Pret> pretsEnCours = pretService.findPretEnCoursByAdherent(adherent);
        
        // Préparer les informations sur les prolongements pour chaque prêt
        model.addAttribute("adherent", adherent);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("pretsEnCours", pretsEnCours);
        model.addAttribute("prolongementService", prolongementService);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        return "prolongements/liste";
    }
    
    @PostMapping("/demande/{pretId}")
    public String demanderProlongement(@PathVariable Long pretId,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier si l'adhérent est un membre actif
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        if (!isActiveMember) {
            session.setAttribute("needMembership", true);
            return "redirect:/inscription";
        }
        
        try {
            // Récupérer le prêt
            Optional<Pret> pretOpt = pretService.getPretById(pretId);
            if (pretOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Prêt non trouvé");
                return "redirect:/prolongements";
            }
            
            Pret pret = pretOpt.get();
            
            // Vérifier que le prêt appartient à l'adhérent
            if (!pret.getAdherent().getId().equals(adherent.getId())) {
                redirectAttributes.addFlashAttribute("error", "Ce prêt ne vous appartient pas");
                return "redirect:/prolongements";
            }
            
            // Vérifier que le prêt est en cours
            if (pretService.isPretRendu(pret)) {
                redirectAttributes.addFlashAttribute("error", "Ce prêt a déjà été rendu");
                return "redirect:/prolongements";
            }
            
            // Vérifier si le prêt a déjà un prolongement
            if (prolongementService.hasPretActiveProlongement(pret) || 
                prolongementService.hasPretCompletedProlongement(pret)) {
                redirectAttributes.addFlashAttribute("error", "Ce prêt a déjà un prolongement en cours ou accepté");
                return "redirect:/prolongements";
            }
            
            // Créer la demande de prolongement
            prolongementService.createProlongementRequest(pret);
            
            redirectAttributes.addFlashAttribute("success", "Votre demande de prolongement a été enregistrée et sera traitée par un bibliothécaire");
            return "redirect:/prolongements";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la demande: " + e.getMessage());
            return "redirect:/prolongements";
        }
    }
    
    @PostMapping("/annuler/{prolongementId}")
    public String annulerProlongement(@PathVariable Long prolongementId,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        try {
            // Récupérer le prolongement
            Optional<Prolongement> prolongementOpt = prolongementService.getProlongementById(prolongementId);
            if (prolongementOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Prolongement non trouvé");
                return "redirect:/prolongements";
            }
            
            Prolongement prolongement = prolongementOpt.get();
            
            // Vérifier que le prolongement appartient à l'adhérent
            if (!prolongement.getPret().getAdherent().getId().equals(adherent.getId())) {
                redirectAttributes.addFlashAttribute("error", "Ce prolongement ne vous appartient pas");
                return "redirect:/prolongements";
            }
            
            // Vérifier que le prolongement est en attente
            if (!prolongementService.isProlongementEnAttente(prolongement)) {
                redirectAttributes.addFlashAttribute("error", "Ce prolongement n'est plus en attente");
                return "redirect:/prolongements";
            }
            
            // Annuler le prolongement
            prolongementService.cancelProlongement(prolongementId);
            
            redirectAttributes.addFlashAttribute("success", "Votre demande de prolongement a été annulée");
            return "redirect:/prolongements";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation: " + e.getMessage());
            return "redirect:/prolongements";
        }
    }
}
