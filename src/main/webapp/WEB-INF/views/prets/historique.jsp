<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historique des prêts - Bibliothèque</title>
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
                <h1>Historique des prêts</h1>
                <div class="header-actions">
                    <a href="<c:url value='/prets'/>" class="btn btn-icon">
                        <i class="fas fa-arrow-left"></i> Retour aux prêts
                    </a>
                </div>
            </header>
            
            <div class="filters-container">
                <div class="filters-header">
                    <h3><i class="fas fa-filter"></i> Filtres</h3>
                    <button class="toggle-btn" onclick="toggleFilters()">
                        <i class="fas fa-chevron-down"></i>
                    </button>
                </div>
                <div class="filters-body" id="filtersBody">
                    <form id="filterForm" method="get" action="<c:url value='/prets/historique'/>">
                        <div class="horizontal-filters">
                            <div class="filter-row">
                                <div class="filter-group">
                                    <label for="search">Recherche</label>
                                    <input type="text" id="search" name="search" class="filter-select" 
                                           value="${currentSearch}" placeholder="Titre ou auteur...">
                                </div>
                                <div class="filter-group">
                                    <label for="statutFilter">Statut</label>
                                    <select id="statutFilter" name="statutFilter" class="filter-select">
                                        <option value="">Tous les statuts</option>
                                        <c:forEach items="${statuts}" var="statut">
                                            <option value="${statut.nom}" 
                                                    ${currentStatutFilter == statut.nom ? 'selected' : ''}>
                                                ${statut.nom}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="filter-row">
                                <div class="filter-group">
                                    <label for="dateDebut">Du</label>
                                    <input type="date" id="dateDebut" name="dateDebut" 
                                           value="${currentDateDebut}">
                                </div>
                                <div class="filter-group">
                                    <label for="dateFin">Au</label>
                                    <input type="date" id="dateFin" name="dateFin" 
                                           value="${currentDateFin}">
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
                    </form>
                </div>
            </div>
            
            <div class="dashboard">
                <div class="quick-actions">
                    <h2>Tous mes prêts <span class="pret-count">(${prets.size()} résultat(s))</span></h2>
                    <c:choose>
                        <c:when test="${not empty prets}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Auteur</th>
                                            <th>Date d'emprunt</th>
                                            <th>Retour prévu</th>
                                            <th>Statut</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${prets}" var="pret">
                                            <tr>
                                                <td>${pret.exemplaire.livre.titre}</td>
                                                <td>${pret.exemplaire.livre.auteur}</td>
                                                <td>${pret.datePret}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${pret.dateRetourPrevue != null}">
                                                            ${pret.dateRetourPrevue}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:forEach items="${pret.historiquePrets}" var="historique" varStatus="loop">
                                                        <c:if test="${loop.last}">
                                                            <span class="status-badge 
                                                                ${historique.statusPret.nom == 'EN_COURS' ? 'status-active' :
                                                                  historique.statusPret.nom == 'RENDU' ? 'status-completed' :
                                                                  historique.statusPret.nom == 'EN_RETARD' ? 'status-late' :
                                                                  'status-default'}">
                                                                ${historique.statusPret.nom}
                                                            </span>
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td>
                                                    <button class="btn btn-icon btn-sm" onclick="showPretDetails(${pret.id})">
                                                        <i class="fas fa-info-circle"></i>
                                                    </button>
                                                    <c:set var="isEnCours" value="false" />
                                                    <c:forEach items="${pret.historiquePrets}" var="historique" varStatus="loop">
                                                        <c:if test="${loop.last && historique.statusPret.nom == 'EN_COURS'}">
                                                            <c:set var="isEnCours" value="true" />
                                                        </c:if>
                                                    </c:forEach>
                                                    <c:if test="${isEnCours}">
                                                        <button class="btn btn-icon btn-sm btn-warning" onclick="openReturnModal(${pret.id}, '${pret.exemplaire.livre.titre}')">
                                                            <i class="fas fa-undo"></i>
                                                        </button>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-search" style="font-size: 48px; color: #bdbdbd; margin-bottom: 20px;"></i>
                                <h3>Aucun prêt trouvé</h3>
                                <p>Essayez de modifier vos critères de recherche ou d'effacer les filtres.</p>
                                <button class="btn btn-primary" style="margin-top: 20px;" onclick="clearFilters()">
                                    <i class="fas fa-times"></i> Effacer les filtres
                                </button>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>
    
    <!-- Modal de retour de prêt -->
    <div id="returnModal" class="modal" style="display: none;">
        <div class="modal-content">
            <span class="close-modal" onclick="closeReturnModal()">&times;</span>
            <h2>Retourner un livre</h2>
            <p id="returnModalText">Êtes-vous sûr de vouloir retourner ce livre ?</p>
            
            <form id="returnForm" action="" method="post">
                <div class="form-group">
                    <label for="dateRetour">Date de retour</label>
                    <input type="date" id="dateRetour" name="dateRetour" required value="${java.time.LocalDate.now()}">
                </div>
                
                <div class="form-actions">
                    <button type="button" class="btn btn-outline" onclick="closeReturnModal()">Annuler</button>
                    <button type="submit" class="btn btn-primary">Confirmer le retour</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation d'entrée pour le tableau
            const table = document.querySelector('.data-table');
            if (table) {
                setTimeout(() => {
                    table.classList.add('fade-in');
                }, 200);
            }
        });
        
        function toggleFilters() {
            const filtersBody = document.getElementById('filtersBody');
            const toggleBtn = document.querySelector('.toggle-btn i');
            
            filtersBody.classList.toggle('expanded');
            toggleBtn.classList.toggle('fa-chevron-down');
            toggleBtn.classList.toggle('fa-chevron-up');
        }
        
        function clearFilters() {
            window.location.href = '<c:url value="/prets/historique"/>';
        }
        
        function showPretDetails(pretId) {
            // Redirection vers la page de détails du prêt
            window.location.href = '<c:url value="/prets/"/>'+pretId;
        }
        
        function openReturnModal(pretId, titre) {
            document.getElementById('returnModalText').innerText = `Êtes-vous sûr de vouloir retourner "${titre}" ?`;
            document.getElementById('returnForm').action = '<c:url value="/prets/retourner/"/>'+pretId;
            document.getElementById('returnModal').style.display = 'block';
        }
        
        function closeReturnModal() {
            document.getElementById('returnModal').style.display = 'none';
        }
        
        // Fermer la modal si l'utilisateur clique en dehors
        window.onclick = function(event) {
            const modal = document.getElementById('returnModal');
            if (event.target == modal) {
                closeReturnModal();
            }
        }
    </script>
    
    <style>
        .table-responsive {
            overflow-x: auto;
        }
        
        .data-table {
            width: 100%;
            border-collapse: collapse;
            opacity: 0;
            transform: translateY(20px);
            transition: opacity 0.5s ease, transform 0.5s ease;
        }
        
        .data-table.fade-in {
            opacity: 1;
            transform: translateY(0);
        }
        
        .data-table th, .data-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .data-table th {
            background-color: #f5f5f5;
            font-weight: 600;
        }
        
        .data-table tr:hover {
            background-color: #f9f9f9;
        }
        
        .pret-count {
            font-size: 0.8em;
            color: #757575;
            font-weight: normal;
        }
        
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            display: inline-block;
        }
        
        .status-active {
            background-color: #e3f2fd;
            color: #1976d2;
        }
        
        .status-completed {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        
        .status-late {
            background-color: #ffe0e0;
            color: #d32f2f;
        }
        
        .status-default {
            background-color: #f5f5f5;
            color: #757575;
        }
        
        .text-muted {
            color: #757575;
            font-style: italic;
        }
        
        .filters-container {
            background-color: #fff;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            overflow: hidden;
        }
        
        .filters-header {
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #eee;
        }
        
        .filters-header h3 {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
        }
        
        .toggle-btn {
            background: none;
            border: none;
            color: #757575;
            cursor: pointer;
            font-size: 16px;
        }
        
        .filters-body {
            max-height: 0;
            overflow: hidden;
            transition: max-height 0.3s ease;
        }
        
        .filters-body.expanded {
            max-height: 500px;
            padding: 20px;
        }
        
        .horizontal-filters {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        
        .filter-row {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .filter-group {
            flex: 1;
            min-width: 200px;
        }
        
        .filter-actions {
            display: flex;
            gap: 10px;
            align-items: flex-end;
        }
        
        .btn-sm {
            padding: 6px 12px;
            font-size: 14px;
        }
        
        .modal {
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .modal-content {
            background-color: #fff;
            border-radius: 10px;
            padding: 30px;
            width: 100%;
            max-width: 500px;
            position: relative;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .close-modal {
            position: absolute;
            top: 15px;
            right: 15px;
            font-size: 24px;
            cursor: pointer;
            color: #aaa;
        }
        
        .close-modal:hover {
            color: #333;
        }
    </style>
</body>
</html>
