<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="home" scope="request" />
        <jsp:include page="fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <h1>Bienvenue, ${adherent.prenom} !</h1>
                <div class="header-actions">
                    <div class="search-bar">
                        <input type="text" placeholder="Rechercher un livre...">
                        <button><i class="fas fa-search"></i></button>
                    </div>
                    <div class="notifications">
                        <i class="fas fa-bell"></i>
                    </div>
                </div>
            </header>
            
            <c:if test="${needMembership}">
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-triangle"></i>
                    <div class="alert-content">
                        <strong>Accès limité !</strong> Cette fonctionnalité est réservée aux membres.
                        <a href="<c:url value='/inscription'/>" class="alert-link">Devenir membre</a>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            
            <div class="membership-status-banner ${isActiveMember ? 'active' : 'inactive'}">
                <div class="status-icon">
                    <i class="fas ${isActiveMember ? 'fa-user-check' : 'fa-user-clock'}"></i>
                </div>
                <div class="status-info">
                    <c:choose>
                        <c:when test="${isActiveMember}">
                            <h3>Membre actif</h3>
                            <p>Votre adhésion est valide jusqu'au ${formattedDates.dateExpiration}</p>
                        </c:when>
                        <c:otherwise>
                            <h3>Non membre</h3>
                            <p>Vous n'avez pas d'adhésion active</p>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="status-action">
                    <c:choose>
                        <c:when test="${isActiveMember}">
                            <a href="<c:url value='/inscription/status'/>" class="btn btn-outline">Voir les détails</a>
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value='/inscription'/>" class="btn btn-primary">Devenir membre</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="dashboard">
                <div class="dashboard-cards">
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-book"></i></div>
                        <div class="card-info">
                            <h3>Mes emprunts</h3>
                            <p>0 livre(s) emprunté(s)</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-clock"></i></div>
                        <div class="card-info">
                            <h3>Mes réservations</h3>
                            <p>0 réservation(s) en cours</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-exclamation-circle"></i></div>
                        <div class="card-info">
                            <h3>Retards</h3>
                            <p>0 livre(s) en retard</p>
                        </div>
                    </div>
                </div>
                
                <div class="quick-actions">
                    <h2>Actions rapides</h2>
                    <div class="action-buttons">
                        <a href="<c:url value='/livres'/>" class="action-button">
                            <i class="fas fa-book"></i>
                            <span>Consulter le catalogue</span>
                        </a>
                        <a href="<c:url value='/prets'/>" class="action-button ${!isActiveMember ? 'disabled' : ''}">
                            <i class="fas fa-bookmark"></i>
                            <span>Voir mes emprunts</span>
                            <c:if test="${!isActiveMember}">
                                <span class="member-only-badge">Membres uniquement</span>
                            </c:if>
                        </a>
                        <a href="<c:url value='/reservations'/>" class="action-button ${!isActiveMember ? 'disabled' : ''}">
                            <i class="fas fa-clock"></i>
                            <span>Gérer mes réservations</span>
                            <c:if test="${!isActiveMember}">
                                <span class="member-only-badge">Membres uniquement</span>
                            </c:if>
                        </a>
                    </div>
                </div>
                
                <div class="recent-books">
                    <h2>Nouveautés</h2>
                    <div class="book-list">
                        <p class="empty-state">Les nouveautés apparaîtront ici</p>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation des cartes du dashboard
            const cards = document.querySelectorAll('.card');
            cards.forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('slide-in');
                }, 100 * index);
            });
            
            // Animation des boutons d'action
            const actionButtons = document.querySelectorAll('.action-button');
            actionButtons.forEach((button, index) => {
                setTimeout(() => {
                    button.classList.add('fade-in');
                }, 300 + (100 * index));
            });
            
            // Animation de la bannière d'adhésion
            const membershipBanner = document.querySelector('.membership-status-banner');
            setTimeout(() => {
                membershipBanner.classList.add('slide-in');
            }, 200);
        });
    </script>
</body>
</html>
