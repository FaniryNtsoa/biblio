<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="inscription" scope="request" />
        <jsp:include page="../fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <h1>Inscription membre</h1>
                <div class="header-actions">
                    <a href="<c:url value='/home'/>" class="btn btn-icon">
                        <i class="fas fa-home"></i> Retour à l'accueil
                    </a>
                </div>
            </header>
            
            <div class="membership-container">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                    </div>
                </c:if>
                
                <c:if test="${not empty membershipMessage}">
                    <div class="alert alert-warning">
                        <i class="fas fa-exclamation-triangle"></i> ${membershipMessage}
                    </div>
                </c:if>
                
                <c:choose>
                    <c:when test="${isActiveMember}">
                        <div class="alert alert-success">
                            <i class="fas fa-check-circle"></i> Vous êtes déjà membre actif jusqu'au 
                            <strong>${formattedDates.dateExpiration}</strong>
                        </div>
                        
                        <div class="membership-info active-member">
                            <div class="membership-icon">
                                <i class="fas fa-user-check"></i>
                            </div>
                            <div class="membership-details">
                                <h2>Votre adhésion est active</h2>
                                <p>Vous avez accès à toutes les fonctionnalités de la bibliothèque.</p>
                                <p>Date d'inscription: ${formattedDates.dateInscription}</p>
                                <p>Date d'expiration: ${formattedDates.dateExpiration}</p>
                            </div>
                        </div>
                        
                        <div class="membership-actions">
                            <a href="<c:url value='/home'/>" class="btn btn-primary">
                                <i class="fas fa-arrow-left"></i> Retour à l'accueil
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="membership-info">
                            <div class="membership-icon inactive">
                                <i class="fas fa-user-clock"></i>
                            </div>
                            <div class="membership-details">
                                <h2>Devenez membre de la bibliothèque</h2>
                                <p>En devenant membre, vous pourrez:</p>
                                <ul>
                                    <li>Emprunter des livres</li>
                                    <li>Réserver des ouvrages</li>
                                    <li>Accéder à votre historique</li>
                                    <li>Et bien plus encore!</li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="membership-form">
                            <h3>Choisissez votre durée d'adhésion</h3>
                            <form action="<c:url value='/inscription'/>" method="post">
                                <div class="membership-period">
                                    <div class="form-group">
                                        <label for="dateExpiration">Date de fin d'adhésion</label>
                                        <div class="input-date">
                                            <i class="fas fa-calendar-alt"></i>
                                            <input type="date" id="dateExpiration" name="dateExpiration" required 
                                                min="${java.time.LocalDate.now().plusDays(1)}" 
                                                value="${java.time.LocalDate.now().plusMonths(3)}">
                                        </div>
                                        <p class="form-help">La date de début est automatiquement fixée à aujourd'hui</p>
                                    </div>
                                    
                                    <div class="duration-presets">
                                        <button type="button" class="btn btn-outline duration-btn" data-months="1">1 mois</button>
                                        <button type="button" class="btn btn-outline duration-btn" data-months="3">3 mois</button>
                                        <button type="button" class="btn btn-outline duration-btn" data-months="6">6 mois</button>
                                        <button type="button" class="btn btn-outline duration-btn" data-months="12">1 an</button>
                                    </div>
                                </div>
                                
                                <div class="form-actions">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-check"></i> Valider mon inscription
                                    </button>
                                </div>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Gestion des boutons de durée prédéfinie
            const durationBtns = document.querySelectorAll('.duration-btn');
            const dateInput = document.getElementById('dateExpiration');
            
            if (dateInput && durationBtns.length > 0) {
                durationBtns.forEach(btn => {
                    btn.addEventListener('click', function() {
                        const months = parseInt(this.getAttribute('data-months'));
                        const today = new Date();
                        const targetDate = new Date(today);
                        targetDate.setMonth(today.getMonth() + months);
                        
                        // Format the date as YYYY-MM-DD for the date input
                        const formattedDate = targetDate.toISOString().split('T')[0];
                        dateInput.value = formattedDate;
                        
                        // Highlight the selected button
                        durationBtns.forEach(b => b.classList.remove('active'));
                        this.classList.add('active');
                    });
                });
                
                // Activer le bouton 3 mois par défaut
                const defaultBtn = document.querySelector('[data-months="3"]');
                if (defaultBtn) {
                    defaultBtn.click();
                }
            }
        });
    </script>
</body>
</html>
