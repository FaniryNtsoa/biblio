<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogue des livres - Bibliothèque</title>
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
                <h1>Catalogue des livres</h1>
                <div class="header-actions">
                    <div class="search-bar-large">
                        <input type="text" id="searchInput" placeholder="Rechercher un livre..." value="${currentSearch}">
                        <button onclick="searchLivres()"><i class="fas fa-search"></i></button>
                    </div>
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
            
            <div class="livre-container">
                <!-- Filtres -->
                <div class="filters-container">
                    <div class="filters-header">
                        <h3><i class="fas fa-filter"></i> Filtres</h3>
                        <button class="toggle-btn" onclick="toggleFilters()">
                            <i class="fas fa-chevron-down"></i>
                        </button>
                    </div>
                    <div class="filters-body" id="filtersBody">
                        <form id="filterForm" method="get" action="<c:url value='/livres'/>">
                            <div class="horizontal-filters">
                                <div class="filter-row">
                                    <div class="filter-group">
                                        <label for="auteur">Auteur</label>
                                        <input type="text" id="auteur" name="auteur" class="filter-select" 
                                               value="${currentAuteur}" placeholder="Nom de l'auteur">
                                    </div>
                                    <div class="filter-group">
                                        <label for="categorie">Catégorie</label>
                                        <select id="categorie" name="categorie" class="filter-select">
                                            <option value="">Toutes les catégories</option>
                                            <c:forEach items="${categories}" var="categorie">
                                                <option value="${categorie.id}" 
                                                        ${currentCategorieId == categorie.id ? 'selected' : ''}>
                                                    ${categorie.nom}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="filter-group">
                                        <label for="langue">Langue</label>
                                        <select id="langue" name="langue" class="filter-select">
                                            <option value="">Toutes les langues</option>
                                            <c:forEach items="${langues}" var="langue">
                                                <option value="${langue}" 
                                                        ${currentLangue == langue ? 'selected' : ''}>
                                                    ${langue}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="filter-row">
                                    <div class="filter-group year-range">
                                        <label>Année de publication</label>
                                        <div class="year-inputs">
                                            <input type="number" name="anneeMin" placeholder="De" 
                                                   value="${currentAnneeMin}" min="1900" max="2024">
                                            <span>à</span>
                                            <input type="number" name="anneeMax" placeholder="À" 
                                                   value="${currentAnneeMax}" min="1900" max="2024">
                                        </div>
                                    </div>
                                    <div class="filter-actions">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            <i class="fas fa-search"></i> Filtrer
                                        </button>
                                        <button type="button" class="btn btn-secondary btn-sm" onclick="clearFilters()">
                                            <i class="fas fa-times"></i> Effacer
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="search" value="${currentSearch}">
                        </form>
                    </div>
                </div>
                
                <!-- Informations de recherche -->
                <c:if test="${not empty currentSearch or not empty currentAuteur or not empty currentCategorieId or not empty currentLangue or not empty currentAnneeMin or not empty currentAnneeMax}">
                    <div class="search-info">
                        <div class="search-results">
                            <i class="fas fa-info-circle"></i>
                            <span>${livres.size()}</span> résultat(s) trouvé(s)
                            <c:if test="${not empty currentSearch}">
                                pour "<strong>${currentSearch}</strong>"
                            </c:if>
                            <a href="<c:url value='/livres'/>" class="clear-search" title="Effacer la recherche">
                                <i class="fas fa-times"></i>
                            </a>
                        </div>
                        <div class="results-count">
                            Total: ${livres.size()} livre(s)
                        </div>
                    </div>
                </c:if>
                
                <!-- Grille des livres -->
                <div class="livres-grid">
                    <c:choose>
                        <c:when test="${not empty livres}">
                            <c:forEach items="${livres}" var="livre" varStatus="status">
                                <div class="livre-card" data-delay="${status.index * 100}">
                                    <div class="livre-cover">
                                        <div class="livre-cover-placeholder">
                                            <i class="fas fa-book"></i>
                                        </div>
                                    </div>
                                    <div class="livre-info">
                                        <h3 class="livre-title" title="${livre.titre}">${livre.titre}</h3>
                                        <p class="livre-author">par ${livre.auteur}</p>
                                        <div class="livre-details">
                                            <span><i class="fas fa-calendar"></i> ${livre.dateSortie.year}</span>
                                            <span><i class="fas fa-language"></i> ${livre.langue}</span>
                                            <span><i class="fas fa-child"></i> ${livre.ageMin}+ ans</span>
                                        </div>
                                        <div class="livre-categories">
                                            <c:forEach items="${livre.categories}" var="categorie">
                                                <span class="category-tag">${categorie.nom}</span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <div class="livre-available ${not empty livre.exemplaires ? 'available' : 'unavailable'}">
                                        <c:choose>
                                            <c:when test="${not empty livre.exemplaires}">
                                                <i class="fas fa-check-circle"></i> Disponible
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-times-circle"></i> Non disponible
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="livre-actions" style="padding: 15px; display: flex; gap: 10px;">
                                        <a href="<c:url value='/livres/${livre.id}'/>" class="btn btn-outline" style="flex: 1; text-align: center;">
                                            <i class="fas fa-eye"></i> Voir détails
                                        </a>
                                        <c:if test="${isActiveMember and not empty livre.exemplaires}">
                                            <a href="<c:url value='/prets/nouveau?livreId=${livre.id}'/>" class="btn btn-primary" style="flex: 1; text-align: center;">
                                                <i class="fas fa-book-open"></i> Emprunter
                                            </a>
                                        </c:if>
                                        <c:if test="${not isActiveMember}">
                                            <a href="<c:url value='/inscription'/>" class="btn btn-secondary" style="flex: 1; text-align: center;" onclick="return confirmMembership()">
                                                <i class="fas fa-lock"></i> Devenir membre
                                            </a>
                                        </c:if>
                                        <c:if test="${isActiveMember and empty livre.exemplaires}">
                                            <button class="btn btn-secondary" disabled style="flex: 1;">
                                                <i class="fas fa-times"></i> Non disponible
                                            </button>
                                        </c:if>
                                        <!-- Bouton de réservation -->
                                        <%-- <c:if test="${isActiveMember}">
                                            <a href="<c:url value='/reservations/nouveau?livreId=${livre.id}'/>" class="btn btn-sm btn-secondary" style="flex: 1; text-align: center;">
                                                <i class="fas fa-calendar-alt"></i> Réserver
                                            </a>
                                        </c:if> --%>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="no-results">
                                <i class="fas fa-search"></i>
                                <h3>Aucun livre trouvé</h3>
                                <p>Essayez de modifier vos critères de recherche.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>
    
    <style>
        .livre-actions {
            display: flex;
            gap: 8px;
            margin-top: 10px;
        }
        
        .btn-sm {
            padding: 5px 10px;
            font-size: 12px;
        }
        
        .btn-outline {
            background-color: transparent;
            border: 1px solid #3949ab;
            color: #3949ab;
        }
        
        .btn-outline:hover {
            background-color: #f5f8ff;
        }
    </style>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation des cartes
            const cards = document.querySelectorAll('.livre-card');
            cards.forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add('fade-in');
                }, index * 50);
            });
        });
        
        function searchLivres() {
            const searchInput = document.getElementById('searchInput');
            const url = new URL(window.location.href);
            url.searchParams.set('search', searchInput.value);
            window.location.href = url.toString();
        }
        
        function toggleFilters() {
            const filtersBody = document.getElementById('filtersBody');
            const toggleBtn = document.querySelector('.toggle-btn i');
            
            filtersBody.classList.toggle('expanded');
            toggleBtn.classList.toggle('fa-chevron-down');
            toggleBtn.classList.toggle('fa-chevron-up');
        }
        
        function clearFilters() {
            window.location.href = '<c:url value="/livres"/>';
        }
        
        // Recherche en temps réel
        document.getElementById('searchInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchLivres();
            }
        });
        
        function confirmMembership() {
            return confirm('Vous devez être membre pour emprunter des livres. Souhaitez-vous devenir membre maintenant ?');
        }
    </script>
</body>
</html>
