<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes prêts - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="prets" scope="request" />
        <jsp:include page="../fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <h1>Mes prêts</h1>
                <div class="header-actions">
                    <a href="<c:url value='/prets/nouveau'/>" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Nouveau prêt
                    </a>
                </div>
            </header>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <div class="dashboard">
                <div class="dashboard-cards">
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-book-open"></i></div>
                        <div class="card-info">
                            <h3>Prêts en cours</h3>
                            <p>${prets.size()} prêt(s)</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-clock"></i></div>
                        <div class="card-info">
                            <h3>À rendre bientôt</h3>
                            <p>0 livre(s)</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-exclamation-triangle"></i></div>
                        <div class="card-info">
                            <h3>En retard</h3>
                            <p>0 livre(s)</p>
                        </div>
                    </div>
                </div>
                
                <div class="quick-actions">
                    <h2>Mes prêts actuels</h2>
                    <c:choose>
                        <c:when test="${not empty prets}">
                            <div class="prets-list" style="display: grid; gap: 20px;">
                                <c:forEach items="${prets}" var="pret" varStatus="status">
                                    <div class="pret-card" style="background: white; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); padding: 20px; opacity: 0; transform: translateY(20px); transition: all 0.5s ease;" data-delay="${status.index * 100}">
                                        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px;">
                                            <div style="flex: 1;">
                                                <h3 style="color: #333; margin-bottom: 5px;">Prêt #${pret.id}</h3>
                                                <p style="color: #666; margin-bottom: 10px;">
                                                    <i class="fas fa-calendar"></i> 
                                                    Date de prêt: <fmt:formatDate value="${pret.datePret}" pattern="dd/MM/yyyy"/>
                                                </p>
                                                <p style="color: #666;">
                                                    <i class="fas fa-tag"></i> 
                                                    Type: ${pret.typePret.nom} (${pret.typePret.duree} jours)
                                                </p>
                                            </div>
                                            <div class="status-badge" style="background-color: #e8f5e9; color: #2e7d32; padding: 5px 10px; border-radius: 15px; font-size: 12px; font-weight: 500;">
                                                <i class="fas fa-check-circle"></i> Actif
                                            </div>
                                        </div>
                                        
                                        <div class="pret-actions" style="display: flex; gap: 10px; margin-top: 15px;">
                                            <button class="btn btn-outline" style="flex: 1;">
                                                <i class="fas fa-info-circle"></i> Détails
                                            </button>
                                            <button class="btn btn-secondary" style="flex: 1;">
                                                <i class="fas fa-clock"></i> Prolonger
                                            </button>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-book-open" style="font-size: 48px; color: #bdbdbd; margin-bottom: 20px;"></i>
                                <h3>Aucun prêt en cours</h3>
                                <p>Vous n'avez actuellement aucun livre emprunté.</p>
                                <a href="<c:url value='/prets/nouveau'/>" class="btn btn-primary" style="margin-top: 20px;">
                                    <i class="fas fa-plus"></i> Emprunter un livre
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
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
            
            // Animation des cartes de prêts
            const pretCards = document.querySelectorAll('.pret-card');
            pretCards.forEach((card, index) => {
                setTimeout(() => {
                    card.style.opacity = '1';
                    card.style.transform = 'translateY(0)';
                }, 300 + (100 * index));
            });
        });
    </script>
</body>
</html>
