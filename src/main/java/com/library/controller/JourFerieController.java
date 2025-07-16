package com.library.controller;

import com.library.model.JourFerie;
import com.library.service.DateCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/jours-feries")
public class JourFerieController {

    private final DateCalculationService dateCalculationService;

    @Autowired
    public JourFerieController(DateCalculationService dateCalculationService) {
        this.dateCalculationService = dateCalculationService;
    }

    @GetMapping
    public String getAllJoursFeries(Model model) {
        List<JourFerie> joursFeries = dateCalculationService.getAllJoursFeries();
        model.addAttribute("joursFeries", joursFeries);
        return "admin/jours-feries/liste";
    }

    @GetMapping("/ajouter")
    public String showAddForm() {
        return "admin/jours-feries/ajouter";
    }

    @PostMapping("/ajouter")
    public String ajouterJourFerie(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String nom,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {
        
        try {
            dateCalculationService.ajouterJourFerie(date, nom, description);
            redirectAttributes.addFlashAttribute("success", "Jour férié ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du jour férié: " + e.getMessage());
        }
        
        return "redirect:/admin/jours-feries";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimerJourFerie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            dateCalculationService.supprimerJourFerie(id);
            redirectAttributes.addFlashAttribute("success", "Jour férié supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du jour férié: " + e.getMessage());
        }
        
        return "redirect:/admin/jours-feries";
    }
    
    @GetMapping("/api/check")
    @ResponseBody
    public ResponseEntity<?> isJourOuvre(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isOuvre = dateCalculationService.isJourOuvre(date);
        return ResponseEntity.ok().body(Map.of("date", date, "jourOuvre", isOuvre));
    }
}
