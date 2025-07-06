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
                <div class="back-button">
                    <a href="<c:url value='/livres'/>" class="btn btn-icon">
                        <i class="fas fa-arrow-left"></i> Retour au catalogue
                    </a>
                </div>
            </header>
            
            <div class="livre-detail-container">
                <div class="livre-detail-header">
                    <div class="livre-detail-cover">
                        <div class="livre-cover-placeholder large">
                            <i class="fas fa-book"></i>
                        </div>
                    </div>
                    <div class="livre-detail-info">
                        <h1>${livre.titre}</h1>
                        <h2>par ${livre.auteur}</h2>
                        
                        <div class="livre-detail-meta">
                            <span class="meta-item">
                                <i class="fas fa-calendar-alt"></i> Année: ${livre.dateSortie.year}
                            </span>
                            <span class="meta-item">
                                <i class="fas fa-globe"></i> Langue: ${livre.langue}
                            </span>
                            <span class="meta-item">
                                <i class="fas fa-child"></i> Âge minimum: ${livre.ageMin} ans
                            </span>
                            <span class="meta-item">
                                <i class="fas fa-building"></i> Édition: ${livre.edition}
                            </span>
                            <c:if test="${not empty livre.isbn}">
                                <span class="meta-item">
                                    <i class="fas fa-barcode"></i> ISBN: ${livre.isbn}
                                </span>
                            </c:if>
                        </div>
                        
                        <div class="livre-detail-categories">
                            <h3>Catégories:</h3>
                            <div class="category-tags">
                                <c:forEach items="${livre.categories}" var="categorie">
                                    <span class="category-tag">${categorie.nom}</span>
                                </c:forEach>
                            </div>
                        </div>
                        
                    </div>
                </div>
                
                <div class="livre-detail-resume">
                    <h3>Résumé</h3>
                    <div class="livre-resume-content">
                        <c:choose>
                            <c:when test="${not empty livre.resume}">
                                <p>${livre.resume}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="no-resume">Aucun résumé disponible pour ce livre.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation d'entrée pour les éléments de la page de détail
            const elements = [
                document.querySelector('.livre-detail-cover'),
                document.querySelector('.livre-detail-info'),
                document.querySelector('.livre-detail-resume')
            ];
            
            elements.forEach((element, index) => {
                setTimeout(() => {
                    element.classList.add('fade-in');
                }, 200 * index);
            });
        });
    </script>
</body>
</html>
