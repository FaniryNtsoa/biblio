package com.library.controller;

import com.library.model.Adherent;
import com.library.model.JourFerie;
import com.library.service.DateCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jours-feries")
public class JourFeriePublicController {

    private final DateCalculationService dateCalculationService;

    @Autowired
    public JourFeriePublicController(DateCalculationService dateCalculationService) {
        this.dateCalculationService = dateCalculationService;
    }

    @GetMapping
    public String showJoursFeries(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Récupérer tous les jours fériés
        List<JourFerie> joursFeries = dateCalculationService.getAllJoursFeries();
        
        // Filtrer pour ne garder que les jours fériés à venir
        LocalDate today = LocalDate.now();
        joursFeries = joursFeries.stream()
                    .filter(jourFerie -> !jourFerie.getDate().isBefore(today))
                    .collect(Collectors.toList());
        
        model.addAttribute("adherent", adherent);
        model.addAttribute("joursFeries", joursFeries);
        
        return "jours-feries/liste";
    }
}
