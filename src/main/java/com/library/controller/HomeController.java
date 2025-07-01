package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        // Si l'utilisateur est déjà connecté, rediriger vers le tableau de bord
        if (session.getAttribute("userId") != null) {
            return "redirect:/dashboard";
        }
        
        // Sinon rediriger vers la page de connexion
        return "redirect:/login";
    }
}
