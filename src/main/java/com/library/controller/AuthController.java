package com.library.controller;

import com.library.model.Adherent;
import com.library.model.TypeAdherent;
import com.library.service.AdherentService;
import com.library.service.TypeAdherentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class AuthController {

    private final AdherentService adherentService;
    private final TypeAdherentService typeAdherentService;

    @Autowired
    public AuthController(AdherentService adherentService, TypeAdherentService typeAdherentService) {
        this.adherentService = adherentService;
        this.typeAdherentService = typeAdherentService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, 
                              @RequestParam String motDePasse,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Adherent adherent = adherentService.findByEmail(email);
        
        if (adherent != null && adherentService.checkMotDePasse(adherent.getId(), motDePasse)) {
            // Authentification réussie
            session.setAttribute("adherent", adherent);
            session.setAttribute("userId", adherent.getId());
            session.setAttribute("userRole", "adherent");
            
            return "redirect:/dashboard";
        } else {
            // Authentification échouée
            redirectAttributes.addFlashAttribute("error", "Email ou mot de passe incorrect");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        List<TypeAdherent> typeAdherents = typeAdherentService.getAllTypeAdherents();
        model.addAttribute("typeAdherents", typeAdherents);
        model.addAttribute("adherent", new Adherent());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute Adherent adherent,
                                     @RequestParam Long typeAdherentId,
                                     RedirectAttributes redirectAttributes) {
        // Vérifier si l'email existe déjà
        if (adherentService.existsByEmail(adherent.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Cet email est déjà utilisé");
            return "redirect:/register";
        }
        
        // Définir le type d'adhérent
        TypeAdherent typeAdherent = typeAdherentService.getTypeAdherentById(typeAdherentId)
                .orElseThrow(() -> new RuntimeException("Type d'adhérent non trouvé"));
        adherent.setTypeAdherent(typeAdherent);
        
        // Enregistrer l'adhérent
        adherentService.saveAdherent(adherent);
        
        redirectAttributes.addFlashAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
