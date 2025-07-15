package com.library.controller;

import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.Categorie;
import com.library.service.LivreService;
import com.library.service.CategorieService;
import com.library.service.InscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/livres")
public class LivreController {

    private final LivreService livreService;
    private final CategorieService categorieService;
    private final InscriptionService inscriptionService;

    @Autowired
    public LivreController(LivreService livreService, 
                          CategorieService categorieService,
                          InscriptionService inscriptionService) {
        this.livreService = livreService;
        this.categorieService = categorieService;
        this.inscriptionService = inscriptionService;
    }

    @GetMapping
    public String listLivres(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "auteur", required = false) String auteur,
            @RequestParam(value = "categorie", required = false) Long categorieId,
            @RequestParam(value = "langue", required = false) String langue,
            @RequestParam(value = "anneeMin", required = false) Integer anneeMin,
            @RequestParam(value = "anneeMax", required = false) Integer anneeMax,
            HttpSession session,
            Model model) {
        
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        // Vérifier le statut d'adhésion
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        
        // Récupérer tous les livres
        List<Livre> livres = livreService.getAllLivres();
        
        // Appliquer les filtres
        if (search != null && !search.trim().isEmpty()) {
            livres = livres.stream()
                    .filter(livre -> livre.getTitre().toLowerCase().contains(search.toLowerCase()) ||
                                   livre.getAuteur().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (auteur != null && !auteur.trim().isEmpty()) {
            livres = livres.stream()
                    .filter(livre -> livre.getAuteur().toLowerCase().contains(auteur.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (categorieId != null) {
            livres = livres.stream()
                    .filter(livre -> livre.getCategories().stream()
                            .anyMatch(cat -> cat.getId().equals(categorieId)))
                    .collect(Collectors.toList());
        }
        
        if (langue != null && !langue.trim().isEmpty()) {
            livres = livres.stream()
                    .filter(livre -> livre.getLangue().equalsIgnoreCase(langue))
                    .collect(Collectors.toList());
        }
        
        if (anneeMin != null) {
            livres = livres.stream()
                    .filter(livre -> livre.getDateSortie().getYear() >= anneeMin)
                    .collect(Collectors.toList());
        }
        
        if (anneeMax != null) {
            livres = livres.stream()
                    .filter(livre -> livre.getDateSortie().getYear() <= anneeMax)
                    .collect(Collectors.toList());
        }
        
        // Récupérer toutes les catégories pour les filtres
        List<Categorie> categories = categorieService.getAllCategories();
        
        // Récupérer les langues disponibles
        List<String> langues = livreService.getAllLivres().stream()
                .map(Livre::getLangue)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        model.addAttribute("livres", livres);
        model.addAttribute("categories", categories);
        model.addAttribute("langues", langues);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("adherent", adherent);
        
        // Conserver les valeurs des filtres
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentAuteur", auteur);
        model.addAttribute("currentCategorieId", categorieId);
        model.addAttribute("currentLangue", langue);
        model.addAttribute("currentAnneeMin", anneeMin);
        model.addAttribute("currentAnneeMax", anneeMax);
        
        return "livres/liste";
    }

    @GetMapping("/{id}")
    public String detailLivre(@PathVariable Long id, HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }
        
        Optional<Livre> livreOpt = livreService.getLivreById(id);
        if (livreOpt.isEmpty()) {
            return "redirect:/livres";
        }
        
        Livre livre = livreOpt.get();
        boolean isActiveMember = inscriptionService.isAdherentActiveMember(adherent);
        
        // Vérifier la disponibilité (simplifié pour le moment)
        boolean isAvailable = !livre.getExemplaires().isEmpty();
        
        model.addAttribute("livre", livre);
        model.addAttribute("isActiveMember", isActiveMember);
        model.addAttribute("isAvailable", isAvailable);
        model.addAttribute("adherent", adherent);
        
        return "livres/detail";
    }

    //api pour récupérer des livres et ses détails
    @GetMapping("/api/all")
    @ResponseBody
    public List<Livre> getAllLivresApi() {
        return livreService.getAllLivres();
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getLivreApi(@PathVariable Long id) {
        Optional<Livre> livre = livreService.getLivreById(id);
        if (livre.isPresent()) {
            return ResponseEntity.ok(livre.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Livre non trouvé", "id", id));
        }
    }
}
