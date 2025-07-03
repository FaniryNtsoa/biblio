package com.library.controller;

import com.library.model.Livre;
import com.library.model.Categorie;
import com.library.model.Adherent;
import com.library.service.LivreService;
import com.library.service.CategorieService;
import com.library.service.ExemplaireService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/livres")
public class LivreController {

    private final LivreService livreService;
    private final CategorieService categorieService;
    private final ExemplaireService exemplaireService;

    @Autowired
    public LivreController(LivreService livreService, CategorieService categorieService, ExemplaireService exemplaireService) {
        this.livreService = livreService;
        this.categorieService = categorieService;
        this.exemplaireService = exemplaireService;
    }

    @GetMapping
    public String listLivres(
            @RequestParam(required = false) String recherche,
            @RequestParam(required = false) Long categorieId,
            @RequestParam(required = false) String langue,
            @RequestParam(required = false) Integer ageMin,
            @RequestParam(required = false) Integer anneeDebut,
            @RequestParam(required = false) Integer anneeFin,
            HttpSession session,
            Model model) {

        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }

        // Récupérer tous les livres
        List<Livre> livres = livreService.getAllLivres();

        // Filtrer par recherche (titre ou auteur)
        if (recherche != null && !recherche.trim().isEmpty()) {
            String search = recherche.toLowerCase();
            livres = livres.stream()
                    .filter(livre -> 
                        livre.getTitre().toLowerCase().contains(search) ||
                        livre.getAuteur().toLowerCase().contains(search) ||
                        (livre.getResume() != null && livre.getResume().toLowerCase().contains(search)) ||
                        (livre.getIsbn() != null && livre.getIsbn().toLowerCase().contains(search))
                    )
                    .collect(Collectors.toList());
        }

        // Filtrer par catégorie
        if (categorieId != null) {
            livres = livres.stream()
                    .filter(livre -> livre.getCategories().stream()
                            .anyMatch(categorie -> categorie.getId().equals(categorieId)))
                    .collect(Collectors.toList());
        }

        // Filtrer par langue
        if (langue != null && !langue.trim().isEmpty()) {
            livres = livres.stream()
                    .filter(livre -> livre.getLangue() != null && livre.getLangue().equalsIgnoreCase(langue))
                    .collect(Collectors.toList());
        }

        // Filtrer par âge minimum
        if (ageMin != null) {
            livres = livres.stream()
                    .filter(livre -> livre.getAgeMin() <= ageMin)
                    .collect(Collectors.toList());
        }

        // Filtrer par année de publication
        if (anneeDebut != null || anneeFin != null) {
            int debutAnnee = anneeDebut != null ? anneeDebut : 0;
            int finAnnee = anneeFin != null ? anneeFin : LocalDate.now().getYear();

            livres = livres.stream()
                    .filter(livre -> {
                        int anneeSortie = livre.getDateSortie().getYear();
                        return anneeSortie >= debutAnnee && anneeSortie <= finAnnee;
                    })
                    .collect(Collectors.toList());
        }

        // Récupérer toutes les catégories pour les filtres
        List<Categorie> categories = categorieService.getAllCategories();

        // Récupérer toutes les langues disponibles
        Set<String> langues = livres.stream()
                .map(Livre::getLangue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Récupérer le nombre d'exemplaires disponibles par livre
        Map<Long, Long> nombreExemplaires = new HashMap<>();
        for (Livre livre : livres) {
            nombreExemplaires.put(livre.getId(), exemplaireService.countByLivre(livre));
        }

        model.addAttribute("livres", livres);
        model.addAttribute("categories", categories);
        model.addAttribute("langues", langues);
        model.addAttribute("nombreExemplaires", nombreExemplaires);
        model.addAttribute("recherche", recherche);
        model.addAttribute("categorieId", categorieId);
        model.addAttribute("langue", langue);
        model.addAttribute("ageMin", ageMin);
        model.addAttribute("anneeDebut", anneeDebut);
        model.addAttribute("anneeFin", anneeFin);
        model.addAttribute("adherent", adherent);

        return "livres/liste";
    }

    @GetMapping("/{id}")
    public String detailLivre(@PathVariable Long id, HttpSession session, Model model) {
        // Vérifier si l'utilisateur est connecté
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) {
            return "redirect:/login";
        }

        // Récupérer le livre par son ID
        Optional<Livre> optionalLivre = livreService.getLivreById(id);
        if (optionalLivre.isEmpty()) {
            return "redirect:/livres";
        }

        Livre livre = optionalLivre.get();
        long nombreExemplaires = exemplaireService.countByLivre(livre);

        model.addAttribute("livre", livre);
        model.addAttribute("nombreExemplaires", nombreExemplaires);
        model.addAttribute("adherent", adherent);

        return "livres/detail";
    }
}
