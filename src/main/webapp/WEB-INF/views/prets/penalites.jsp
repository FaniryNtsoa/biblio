<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes pénalités - Bibliothèque</title>
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
                <h1>Mes pénalités</h1>
                <div class="header-actions">
                    <a href="<c:url value='/prets'/>" class="btn btn-icon">
                        <i class="fas fa-arrow-left"></i> Retour aux prêts
                    </a>
                </div>
            </header>
            
            <div class="dashboard">
                <c:if test="${hasPenalites}">
                    <div class="alert alert-danger" style="margin-bottom: 20px;">
                        <i class="fas fa-exclamation-triangle"></i>
                        <div class="alert-content">
                            <strong>Attention!</strong> Vous avez des pénalités actives. 
                            Vous ne pouvez pas emprunter de nouveaux livres jusqu'au ${dateFinPenalite}.
                        </div>
                    </div>
                </c:if>
                
                <div class="penalites-section">
                    <h2>Pénalités actives</h2>
                    
                    <c:choose>
                        <c:when test="${not empty penalitesActives}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date de retard</th>
                                            <th>Jours de retard</th>
                                            <th>Fin de pénalité</th>
                                            <th>Description</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${penalitesActives}" var="penalite">
                                            <tr>
                                                <td>${penalite.pret.exemplaire.livre.titre}</td>
                                                <td>${penalite.datePenalite}</td>
                                                <td>${penalite.nbJoursRetard} jour(s)</td>
                                                <td><strong>${penalite.dateFinPenalite}</strong></td>
                                                <td>${penalite.description}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-check-circle" style="font-size: 48px; color: #4caf50; margin-bottom: 20px;"></i>
                                <h3>Aucune pénalité active</h3>
                                <p>Vous n'avez actuellement aucune pénalité en cours. Vous pouvez emprunter des livres.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="penalites-section" style="margin-top: 40px;">
                    <h2>Historique des pénalités</h2>
                    
                    <c:choose>
                        <c:when test="${not empty toutesLesPenalites}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date de retard</th>
                                            <th>Jours de retard</th>
                                            <th>Fin de pénalité</th>
                                            <th>Statut</th>
                                            <th>Description</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${toutesLesPenalites}" var="penalite">
                                            <tr>
                                                <td>${penalite.pret.exemplaire.livre.titre}</td>
                                                <td>${penalite.datePenalite}</td>
                                                <td>${penalite.nbJoursRetard} jour(s)</td>
                                                <td>${penalite.dateFinPenalite}</td>
                                                <td>
                                                    <span class="status-badge ${penalite.active ? 'status-late' : 'status-completed'}">
                                                        ${penalite.active ? 'Active' : 'Terminée'}
                                                    </span>
                                                </td>
                                                <td>${penalite.description}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-history" style="font-size: 48px; color: #bdbdbd; margin-bottom: 20px;"></i>
                                <h3>Aucun historique de pénalité</h3>
                                <p>Vous n'avez jamais eu de pénalité. Continuez comme ça!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animation d'entrée pour les tableaux
            const tables = document.querySelectorAll('.data-table');
            tables.forEach((table, index) => {
                setTimeout(() => {
                    table.classList.add('fade-in');
                }, 200 * (index + 1));
            });
        });
    </script>
    
    <style>
        .penalites-section {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .penalites-section h2 {
            margin-bottom: 20px;
            color: #333;
            font-size: 18px;
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
        
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            display: inline-block;
        }
        
        .status-late {
            background-color: #ffe0e0;
            color: #d32f2f;
        }
        
        .status-completed {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
    </style>
</body>
</html>
