<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogue de livres - Bibliothèque</title>
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
                <h1>Catalogue de livres</h1>
                <div class="header-actions">
                    <form action="<c:url value='/livres'/>" method="get" class="search-bar-large">
                        <input type="text" name="recherche" value="${recherche}" placeholder="Rechercher par titre, auteur, ISBN, résumé...">
                        <button type="submit"><i class="fas fa-search"></i></button>
                    </form>
                </div>
            </header>
            
            <div class="livre-container">
                <div class="filters-container">
                    <div class="filters-header">
                        <h3><i class="fas fa-filter"></i> Filtres</h3>
                        <button id="toggle-filters" class="toggle-btn">
                            <i class="fas fa-chevron-down"></i>
                        </button>
                    </div>
                    
                    <div class="filters-body">
                        <form action="<c:url value='/livres'/>" method="get" id="filter-form" class="horizontal-filters">
                            <input type="hidden" name="recherche" value="${recherche}">
                            
                            <div class="filter-row">
                                <div class="filter-group">
                                    <label for="categorieId">Catégorie</label>
                                    <select name="categorieId" id="categorieId" class="filter-select">
                                        <option value="">Toutes</option>
                                        <c:forEach items="${categories}" var="categorie">
                                            <option value="${categorie.id}" ${categorieId == categorie.id ? 'selected' : ''}>${categorie.nom}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="filter-group">
                                    <label for="langue">Langue</label>
                                    <select name="langue" id="langue" class="filter-select">
                                        <option value="">Toutes</option>
                                        <c:forEach items="${langues}" var="langueOption">
                                            <option value="${langueOption}" ${langue == langueOption ? 'selected' : ''}>${langueOption}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="filter-group">
                                    <label for="ageMin">Âge minimum</label>
                                    <select name="ageMin" id="ageMin" class="filter-select">
                                        <option value="">Tous</option>
                                        <option value="3" ${ageMin == 3 ? 'selected' : ''}>3+</option>
                                        <option value="6" ${ageMin == 6 ? 'selected' : ''}>6+</option>
                                        <option value="9" ${ageMin == 9 ? 'selected' : ''}>9+</option>
                                        <option value="12" ${ageMin == 12 ? 'selected' : ''}>12+</option>
                                        <option value="15" ${ageMin == 15 ? 'selected' : ''}>15+</option>
                                        <option value="18" ${ageMin == 18 ? 'selected' : ''}>18+</option>
                                    </select>
                                </div>
                                
                                <div class="filter-group year-range">
                                    <label>Année</label>
                                    <div class="year-inputs">
                                        <input type="number" name="anneeDebut" placeholder="De" min="1800" max="2099" step="1" value="${anneeDebut}">
                                        <span>-</span>
                                        <input type="number" name="anneeFin" placeholder="À" min="1800" max="2099" step="1" value="${anneeFin}">
                                    </div>
                                </div>
                            </div>
                            
                            <div class="filter-actions">
                                <button type="submit" class="btn btn-primary btn-sm"><i class="fas fa-filter"></i> Filtrer</button>
                                <a href="<c:url value='/livres'/>" class="btn btn-secondary btn-sm"><i class="fas fa-times"></i> Réinitialiser</a>
                            </div>
                        </form>
                    </div>
                </div>
                
                <div class="search-info">
                    <c:if test="${not empty recherche}">
                        <div class="search-results">
                            <i class="fas fa-search"></i> Résultats pour "<span>${recherche}</span>"
                            <a href="<c:url value='/livres'/>" class="clear-search"><i class="fas fa-times"></i></a>
                        </div>
                    </c:if>
                    <div class="results-count">
                        <c:choose>
                            <c:when test="${empty livres}">
                                Aucun livre trouvé
                            </c:when>
                            <c:otherwise>
                                ${livres.size()} livre(s) trouvé(s)
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="livres-grid">
                    <c:if test="${empty livres}">
                        <div class="no-results">
                            <i class="fas fa-search"></i>
                            <h3>Aucun livre trouvé</h3>
                            <p>Essayez de modifier vos critères de recherche ou de filtrage.</p>
                        </div>
                    </c:if>
                    
                    <c:forEach items="${livres}" var="livre">
                        <div class="livre-card" onclick="location.href='<c:url value='/livres/${livre.id}'/>'">
                            <div class="livre-cover">
                                <div class="livre-cover-placeholder">
                                    <i class="fas fa-book"></i>
                                </div>
                            </div>
                            <div class="livre-info">
                                <h3 class="livre-title">${livre.titre}</h3>
                                <p class="livre-author">${livre.auteur}</p>
                                <div class="livre-details">
                                    <span class="livre-year"><i class="far fa-calendar-alt"></i> ${livre.dateSortie.year}</span>
                                    <span class="livre-lang"><i class="fas fa-globe"></i> ${livre.langue}</span>
                                    <span class="livre-age"><i class="fas fa-child"></i> ${livre.ageMin}+</span>
                                </div>
                                <div class="livre-categories">
                                    <c:forEach items="${livre.categories}" var="categorie" varStatus="status">
                                        <span class="category-tag">${categorie.nom}</span>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation d'apparition des cartes de livres
            const livreCards = document.querySelectorAll('.livre-card');
            livreCards.forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('fade-in');
                }, 50 * index);
            });
            
            // Toggle pour afficher/masquer les filtres sur mobile
            const toggleFiltersBtn = document.getElementById('toggle-filters');
            const filtersBody = document.querySelector('.filters-body');
            
            toggleFiltersBtn.addEventListener('click', function() {
                filtersBody.classList.toggle('expanded');
                this.querySelector('i').classList.toggle('fa-chevron-down');
                this.querySelector('i').classList.toggle('fa-chevron-up');
            });
            
            // Filtres actifs au changement de sélection
            const filterSelects = document.querySelectorAll('.filter-select');
            filterSelects.forEach(select => {
                select.addEventListener('change', function() {
                    document.getElementById('filter-form').submit();
                });
            });
        });
    </script>
</body>
</html>
