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
            
            <c:if test="${not empty warning}">
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-triangle"></i> ${warning}
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <div class="dashboard">
                <c:if test="${hasPenalites}">
                    <div class="alert alert-warning" style="margin-bottom: 20px;">
                        <i class="fas fa-exclamation-triangle"></i>
                        <div class="alert-content">
                            <strong>Pénalité en cours!</strong> En raison de retards, vous ne pouvez pas emprunter de nouveaux livres jusqu'au ${dateFinPenalite}.
                            <a href="<c:url value='/prets/penalites'/>" class="alert-link">Voir les détails</a>
                        </div>
                    </div>
                </c:if>
                
                <div class="dashboard-cards">
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-book-open"></i></div>
                        <div class="card-info">
                            <h3>Prêts en cours</h3>
                            <p>${pretsEnCours} prêt(s)</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-clock"></i></div>
                        <div class="card-info">
                            <h3>À rendre bientôt</h3>
                            <p>${pretsARendreBientot} livre(s)</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-exclamation-triangle"></i></div>
                        <div class="card-info">
                            <h3>En retard</h3>
                            <p>${pretsEnRetard} livre(s)</p>
                        </div>
                    </div>
                    
                    <c:if test="${hasPenalites}">
                        <div class="card" style="background-color: #ffebee; border-left: 4px solid #d32f2f;">
                            <div class="card-icon" style="color: #d32f2f;"><i class="fas fa-ban"></i></div>
                            <div class="card-info">
                                <h3 style="color: #d32f2f;">Pénalité active</h3>
                                <p>Jusqu'au ${dateFinPenalite}</p>
                            </div>
                        </div>
                    </c:if>
                </div>
                
                <div class="filter-tabs">
                    <a href="<c:url value='/prets?statutFilter=en_cours'/>" class="filter-tab ${currentFilter == 'en_cours' ? 'active' : ''}">
                        <i class="fas fa-book-open"></i> En cours
                    </a>
                    <a href="<c:url value='/prets?statutFilter=en_retard'/>" class="filter-tab ${currentFilter == 'en_retard' ? 'active' : ''}">
                        <i class="fas fa-exclamation-triangle"></i> En retard
                    </a>
                    <a href="<c:url value='/prets?statutFilter=retournes'/>" class="filter-tab ${currentFilter == 'retournes' ? 'active' : ''}">
                        <i class="fas fa-check-circle"></i> Retournés récemment
                    </a>
                </div>
                
                <div class="quick-actions">
                    <div class="quick-actions-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                        <h2>Mes prêts ${currentFilter == 'en_cours' ? 'actuels' : currentFilter == 'en_retard' ? 'en retard' : currentFilter == 'retournes' ? 'retournés récemment' : ''}</h2>
                        <a href="<c:url value='/prets/penalites'/>" class="btn btn-outline">
                            <i class="fas fa-exclamation-circle"></i> Voir mes pénalités
                        </a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty prets}">
                            <div class="prets-list" style="display: grid; gap: 20px;">
                                <c:forEach items="${prets}" var="pret" varStatus="status">
                                    <div class="pret-card" style="background: white; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); padding: 20px; opacity: 0; transform: translateY(20px); transition: all 0.5s ease;" data-delay="${status.index * 100}">
                                        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px;">
                                            <div style="flex: 1;">
                                                <h3 style="color: #333; margin-bottom: 5px;">${pret.exemplaire.livre.titre}</h3>
                                                <p style="color: #666; margin-bottom: 5px;">
                                                    par ${pret.exemplaire.livre.auteur}
                                                </p>
                                                <p style="color: #666; margin-bottom: 10px;">
                                                    <i class="fas fa-calendar"></i> 
                                                    Emprunté le: ${pret.datePret}
                                                </p>
                                                <c:if test="${pret.dateRetourPrevue != null}">
                                                    <p style="color: #666;">
                                                        <i class="fas fa-calendar-check"></i> 
                                                        À rendre le: ${pret.dateRetourPrevue}
                                                    </p>
                                                </c:if>
                                            </div>
                                            <div class="status-badge" style="background-color: 
                                                ${currentFilter == 'en_retard' ? '#ffe0e0' : 
                                                  currentFilter == 'retournes' ? '#e8f5e9' : 
                                                  '#e3f2fd'}; 
                                                color: 
                                                ${currentFilter == 'en_retard' ? '#d32f2f' : 
                                                  currentFilter == 'retournes' ? '#2e7d32' : 
                                                  '#1976d2'}; 
                                                padding: 5px 10px; border-radius: 15px; font-size: 12px; font-weight: 500;">
                                                <i class="fas 
                                                    ${currentFilter == 'en_retard' ? 'fa-exclamation-circle' : 
                                                      currentFilter == 'retournes' ? 'fa-check-circle' : 
                                                      'fa-clock'}"></i> 
                                                ${currentFilter == 'en_retard' ? 'En retard' : 
                                                  currentFilter == 'retournes' ? 'Retourné' : 
                                                  'En cours'}
                                            </div>
                                        </div>
                                        
                                        <c:if test="${currentFilter == 'en_cours' || currentFilter == 'en_retard' || currentFilter == 'tous'}">
                                            <div class="pret-actions" style="display: flex; gap: 10px; margin-top: 15px;">
                                                <button class="btn btn-outline" style="flex: 1;" onclick="openDetails(${pret.id})">
                                                    <i class="fas fa-info-circle"></i> Détails
                                                </button>
                                                <c:if test="${currentFilter != 'retournes'}">
                                                    <button class="btn btn-secondary" style="flex: 1;" onclick="openReturnModal(${pret.id}, '${pret.exemplaire.livre.titre}')">
                                                        <i class="fas fa-undo"></i> Retourner
                                                    </button>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-book-open" style="font-size: 48px; color: #bdbdbd; margin-bottom: 20px;"></i>
                                <h3>Aucun prêt ${currentFilter == 'en_cours' ? 'en cours' : currentFilter == 'en_retard' ? 'en retard' : currentFilter == 'retournes' ? 'retourné récemment' : ''}</h3>
                                <p>
                                    <c:choose>
                                        <c:when test="${currentFilter == 'en_cours' || currentFilter == 'tous'}">
                                            Vous n'avez actuellement aucun livre emprunté.
                                        </c:when>
                                        <c:when test="${currentFilter == 'en_retard'}">
                                            Tous vos prêts sont à jour. Merci pour votre ponctualité !
                                        </c:when>
                                        <c:when test="${currentFilter == 'retournes'}">
                                            Vous n'avez pas retourné de livres récemment.
                                        </c:when>
                                    </c:choose>
                                </p>
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
        
        function openDetails(pretId) {
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
        .filter-tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            overflow-x: auto;
            padding-bottom: 5px;
        }
        
        .filter-tab {
            padding: 8px 15px;
            border-radius: 20px;
            background-color: #f5f5f5;
            color: #333;
            text-decoration: none;
            font-size: 14px;
            white-space: nowrap;
            transition: all 0.2s ease;
        }
        
        .filter-tab:hover {
            background-color: #e0e0e0;
        }
        
        .filter-tab.active {
            background-color: #1976d2;
            color: white;
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
