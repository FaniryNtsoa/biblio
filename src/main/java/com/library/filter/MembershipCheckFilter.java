package com.library.filter;

import com.library.model.Adherent;
import com.library.service.InscriptionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class MembershipCheckFilter implements Filter {

    private final InscriptionService inscriptionService;
    
    // Chemins qui nécessitent une inscription active
    private final List<String> protectedPaths = Arrays.asList(
        "/prets", 
        "/reservations", 
        "/historique"
    );
    
    // Chemins publics ou qui ne nécessitent qu'une connexion
    private final List<String> publicPaths = Arrays.asList(
        "/login", 
        "/register", 
        "/css/", 
        "/js/", 
        "/images/", 
        "/livres", 
        "/home", 
        "/inscription", 
        "/logout"
    );

    @Autowired
    public MembershipCheckFilter(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // Vérifier si le chemin demandé nécessite une vérification d'inscription
        if (isProtectedPath(path) && !isPublicPath(path)) {
            HttpSession session = httpRequest.getSession(false);
            
            if (session == null || session.getAttribute("adherent") == null) {
                // L'utilisateur n'est pas connecté, rediriger vers la page de connexion
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                return;
            }
            
            Adherent adherent = (Adherent) session.getAttribute("adherent");
            
            // Vérifier si l'adhérent a une inscription active
            if (!inscriptionService.isAdherentActiveMember(adherent)) {
                // L'adhérent n'est pas un membre actif, rediriger vers la page d'inscription
                httpRequest.getSession().setAttribute("redirectAfterInscription", path);
                httpRequest.getSession().setAttribute("needMembership", true);
                
                // Ajouter un message personnalisé selon la page demandée
                String message = "Cette fonctionnalité nécessite une adhésion active.";
                if (path.startsWith("/prets")) {
                    message = "Vous devez être membre pour emprunter des livres.";
                } else if (path.startsWith("/reservations")) {
                    message = "Vous devez être membre pour faire des réservations.";
                } else if (path.startsWith("/historique")) {
                    message = "Vous devez être membre pour consulter votre historique.";
                }
                
                httpRequest.getSession().setAttribute("membershipMessage", message);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/inscription");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean isProtectedPath(String path) {
        return protectedPaths.stream().anyMatch(path::startsWith);
    }
    
    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }
}
