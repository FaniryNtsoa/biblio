<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Statut d'adhésion - Bibliothèque</title>
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
                <h1>Statut de votre adhésion</h1>
                <div class="header-actions">
                    <a href="<c:url value='/home'/>" class="btn btn-icon">
                        <i class="fas fa-home"></i> Retour à l'accueil
                    </a>
                </div>
            </header>
            
            <div class="membership-container">
                <c:choose>
                    <c:when test="${isActiveMember}">
                        <div class="membership-status active">
                            <div class="status-icon">
                                <i class="fas fa-check-circle"></i>
                            </div>
                            <div class="status-details">
                                <h2>Votre adhésion est active</h2>
                                <p class="status-info">Vous êtes membre de notre bibliothèque jusqu'au <strong>${formattedDates.dateExpiration}</strong></p>
                                <div class="status-benefits">
                                    <h3>Vos avantages :</h3>
                                    <ul>
                                        <li><i class="fas fa-check"></i> Emprunter des livres</li>
                                        <li><i class="fas fa-check"></i> Réserver des ouvrages</li>
                                        <li><i class="fas fa-check"></i> Accéder à votre historique</li>
                                        <li><i class="fas fa-check"></i> Prolonger vos emprunts</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        
                        <div class="membership-info-card">
                            <div class="info-card-header">
                                <h3>Informations sur votre adhésion</h3>
                            </div>
                            <div class="info-card-body">
                                <div class="info-item">
                                    <span class="info-label">Date d'inscription :</span>
                                    <span class="info-value">${formattedDates.dateInscription}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Date d'expiration :</span>
                                    <span class="info-value">${formattedDates.dateExpiration}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Statut :</span>
                                    <span class="info-value status-active">Actif</span>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="membership-status inactive">
                            <div class="status-icon">
                                <i class="fas fa-exclamation-circle"></i>
                            </div>
                            <div class="status-details">
                                <h2>Votre adhésion n'est pas active</h2>
                                <p class="status-info">Vous n'êtes actuellement pas membre de notre bibliothèque ou votre adhésion a expiré.</p>
                                <c:if test="${not empty latestInscription}">
                                    <p>Votre dernière adhésion a expiré le <strong>${formattedDates.dateExpiration}</strong></p>
                                </c:if>
                                <div class="status-restricted">
                                    <h3>Fonctionnalités limitées :</h3>
                                    <ul>
                                        <li><i class="fas fa-check"></i> Consulter le catalogue</li>
                                        <li><i class="fas fa-times"></i> Emprunter des livres</li>
                                        <li><i class="fas fa-times"></i> Réserver des ouvrages</li>
                                        <li><i class="fas fa-times"></i> Accéder à votre historique</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        
                        <div class="membership-actions">
                            <a href="<c:url value='/inscription'/>" class="btn btn-primary">
                                <i class="fas fa-user-plus"></i> Devenir membre
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</body>
</html>
