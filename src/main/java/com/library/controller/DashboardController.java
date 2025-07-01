package com.library.controller;

import com.library.model.Adherent;
import com.library.service.AdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final AdherentService adherentService;

    @Autowired
    public DashboardController(AdherentService adherentService) {
        this.adherentService = adherentService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Récupérer les informations de l'adhérent
        Adherent adherent = adherentService.getAdherentById(userId)
                .orElseThrow(() -> new RuntimeException("Adhérent non trouvé"));
        
        model.addAttribute("adherent", adherent);
        
        return "dashboard";
    }
}
