<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord - Bibliothécaire</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/bibliothecaire.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2><i class="fas fa-book"></i> <span>Bibliothèque</span></h2>
                <p>Espace Bibliothécaire</p>
            </div>
            
            <nav class="sidebar-nav">
                <ul>
                    <li class="active">
                        <a href="<c:url value='/bibliothecaire/dashboard'/>">
                            <i class="fas fa-tachometer-alt"></i> <span>Tableau de bord</span>
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value='/bibliothecaire/prets'/>">
                            <i class="fas fa-book-open"></i> <span>Gestion des prêts</span>
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value='/bibliothecaire/livres'/>">
                            <i class="fas fa-book"></i> <span>Gestion des livres</span>
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value='/bibliothecaire/logout'/>">
                            <i class="fas fa-sign-out-alt"></i> <span>Déconnexion</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </aside>
        
        <main class="content">
            <header class="content-header">
                <h1><i class="fas fa-tachometer-alt"></i> Tableau de bord</h1>
                <div class="user-info">
                    <i class="fas fa-user-tie"></i>
                    <span>Bibliothécaire: ${bibliothecaire.username}</span>
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
            
            <div class="dashboard-section">
                <h2><i class="fas fa-clock"></i> Réservations en attente (${reservationsEnAttente.size()})</h2>
                
                <c:choose>
                    <c:when test="${not empty reservationsEnAttente}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Adhérent</th>
                                        <th>Livre</th>
                                        <th>Date de réservation</th>
                                        <th>Date de la demande</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="reservation" items="${reservationsEnAttente}">
                                        <tr>
                                            <td>${reservation.id}</td>
                                            <td>${reservation.adherent.prenom} ${reservation.adherent.nom}</td>
                                            <td>${reservation.livre.titre}</td>
                                            <td>
                                                <fmt:parseDate value="${reservation.dateReservation}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td>
                                                <c:forEach var="historique" items="${reservation.historiqueStatusReservations}" varStatus="loop">
                                                    <c:if test="${loop.first}">
                                                        <fmt:parseDate value="${historique.dateReservation}" pattern="yyyy-MM-dd" var="parsedDateDemande" type="date" />
                                                        <fmt:formatDate value="${parsedDateDemande}" type="date" pattern="dd/MM/yyyy" />
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td class="actions">
                                                <form action="<c:url value='/bibliothecaire/reservations/${reservation.id}/accept'/>" method="post" style="display:inline;">
                                                    <button type="submit" class="btn btn-sm btn-success">
                                                        <i class="fas fa-check"></i> Accepter
                                                    </button>
                                                </form>
                                                <form action="<c:url value='/bibliothecaire/reservations/${reservation.id}/reject'/>" method="post" style="display:inline;">
                                                    <button type="submit" class="btn btn-sm btn-danger">
                                                        <i class="fas fa-times"></i> Rejeter
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <i class="fas fa-check-circle"></i>
                            <p>Aucune réservation en attente</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            
    
    <style>
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        
        .stat-card {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            padding: 20px;
            display: flex;
            align-items: center;
            transition: all 0.3s ease;
            border-left: 4px solid transparent;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
        }
        
        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            font-size: 24px;
            color: white;
        }
        
        .stat-icon.blue {
            background-color: #3949ab;
            border-color: #3949ab;
        }
        
        .stat-icon.green {
            background-color: #4caf50;
            border-color: #4caf50;
        }
        
        .stat-icon.orange {
            background-color: #ff9800;
            border-color: #ff9800;
        }
        
        .stat-icon.purple {
            background-color: #9c27b0;
            border-color: #9c27b0;
        }
        
        .stat-info h3 {
            font-size: 14px;
            color: #666;
            margin-bottom: 5px;
            font-weight: normal;
        }
        
        .stat-info p {
            font-size: 24px;
            font-weight: 600;
            color: #333;
            margin: 0;
        }
        
        @media (max-width: 768px) {
            .stats-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</body>
</html>
