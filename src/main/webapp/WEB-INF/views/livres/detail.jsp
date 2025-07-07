<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${livre.titre} - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="livres" scope="request" />
        <jsp:include page="../fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <a href="<c:url value='/livres'/>" class="btn btn-icon back-button">
                    <i class="fas fa-arrow-left"></i> Retour au catalogue
                </a>
                <div class="header-actions">
                    <div class="notifications">
                        <i class="fas fa-bell"></i>
                    </div>
                </div>
            </header>
            
            <c:if test="${not empty livre}">
                <div class="livre-detail-container">
                    <div class="livre-detail-header">
                        <div class="livre-detail-cover livre-cover-placeholder large">
                            <i class="fas fa-book"></i>
                        </div>
                        
                        <div class="livre-detail-info">
                            <h1>${livre.titre}</h1>
                            <h2>par ${livre.auteur}</h2>
                            
                            <div class="livre-detail-meta">
                                <div class="meta-item">
                                    <i class="fas fa-calendar-alt"></i>
                                    <fmt:parseDate value="${livre.dateSortie}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                    <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy" />
                                </div>
                                
                                <div class="meta-item">
                                    <i class="fas fa-bookmark"></i>
                                    ${livre.edition}
                                </div>
                                
                                <div class="meta-item">
                                    <i class="fas fa-language"></i>
                                    ${livre.langue}
                                </div>
                                
                                <c:if test="${not empty livre.isbn}">
                                    <div class="meta-item">
                                        <i class="fas fa-barcode"></i>
                                        ISBN: ${livre.isbn}
                                    </div>
                                </c:if>
                                
                                <div class="meta-item">
                                    <i class="fas fa-child"></i>
                                    ${livre.ageMin}+ ans
                                </div>
                            </div>
                            
                            <div class="livre-detail-categories">
                                <h3>Catégories</h3>
                                <div class="category-tags">
                                    <c:forEach var="categorie" items="${livre.categories}">
                                        <span class="category-tag">${categorie.nom}</span>
                                    </c:forEach>
                                </div>
                            </div>
                            
                            <div class="livre-detail-availability">
                                <h3>Disponibilité</h3>
                                <c:choose>
                                    <c:when test="${isAvailable}">
                                        <div class="availability-status available">
                                            <i class="fas fa-check-circle"></i> Disponible
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="availability-status unavailable">
                                            <i class="fas fa-times-circle"></i> Indisponible
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <div class="livre-detail-actions">
                                <c:if test="${isActiveMember and isAvailable}">
                                    <a href="<c:url value='/prets/nouveau?livreId=${livre.id}'/>" class="btn btn-primary">
                                        <i class="fas fa-book-open"></i> Emprunter maintenant
                                    </a>
                                </c:if>
                                <c:if test="${isActiveMember}">
                                    <a href="<c:url value='/reservations/nouveau?livreId=${livre.id}'/>" class="btn btn-secondary">
                                        <i class="fas fa-calendar-alt"></i> Réserver
                                    </a>
                                </c:if>
                                <c:if test="${not isActiveMember}">
                                    <a href="<c:url value='/inscription'/>" class="btn btn-secondary" onclick="return confirmMembership()">
                                        <i class="fas fa-lock"></i> Devenir membre pour emprunter
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    
                    <div class="livre-detail-resume">
                        <h3>Résumé</h3>
                        <div class="livre-resume-content">
                            <c:choose>
                                <c:when test="${not empty livre.resume}">
                                    ${livre.resume}
                                </c:when>
                                <c:otherwise>
                                    <p class="no-resume">Aucun résumé disponible pour ce livre.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:if>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation des éléments
            setTimeout(() => {
                document.querySelector('.livre-detail-cover').classList.add('fade-in');
            }, 100);
            
            setTimeout(() => {
                document.querySelector('.livre-detail-info').classList.add('fade-in');
            }, 300);
            
            setTimeout(() => {
                document.querySelector('.livre-detail-resume').classList.add('fade-in');
            }, 500);
        });
        
        function confirmMembership() {
            return confirm('Vous devez être membre pour emprunter ou réserver des livres. Souhaitez-vous devenir membre maintenant ?');
        }
    </script>
</body>
</html>