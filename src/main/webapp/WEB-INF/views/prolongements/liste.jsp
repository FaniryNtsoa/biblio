<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes prolongements - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="prolongements" scope="request" />
        <jsp:include page="../fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <h1><i class="fas fa-hourglass-half"></i> Mes demandes de prolongement</h1>
                <div class="header-actions">
                    <a href="<c:url value='/prets'/>" class="btn btn-icon">
                        <i class="fas fa-book-open"></i> Mes prêts
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
            
            <div class="section-container">
                <div class="section-header">
                    <h2><i class="fas fa-history"></i> Mes prêts en cours</h2>
                    <p>Vous pouvez demander un prolongement pour vos prêts en cours.</p>
                </div>
                
                <c:choose>
                    <c:when test="${not empty pretsEnCours}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Livre</th>
                                        <th>Date d'emprunt</th>
                                        <th>Date de retour prévue</th>
                                        <th>Statut du prolongement</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="pret" items="${pretsEnCours}">
                                        <tr>
                                            <td>${pret.exemplaire.livre.titre}</td>
                                            <td>
                                                <fmt:parseDate value="${pret.datePret}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td>
                                                <fmt:parseDate value="${pret.dateRetourPrevue}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${prolongementService.hasPretActiveProlongement(pret)}">
                                                        <span class="status-badge pending">
                                                            <i class="fas fa-clock"></i> En attente
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${prolongementService.hasPretCompletedProlongement(pret)}">
                                                        <span class="status-badge success">
                                                            <i class="fas fa-check-circle"></i> Prolongé
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge neutral">
                                                            <i class="fas fa-minus-circle"></i> Aucun
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="actions">
                                                <c:choose>
                                                    <c:when test="${prolongementService.hasPretActiveProlongement(pret)}">
                                                        <c:set var="prolongement" value="${prolongementService.findActiveProlongementByPret(pret).get()}" />
                                                        <form action="<c:url value='/prolongements/annuler/${prolongement.id}'/>" method="post" style="display:inline;">
                                                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir annuler cette demande de prolongement ?')">
                                                                <i class="fas fa-times"></i> Annuler
                                                            </button>
                                                        </form>
                                                    </c:when>
                                                    <c:when test="${prolongementService.hasPretCompletedProlongement(pret)}">
                                                        <span class="disabled-action">
                                                            <i class="fas fa-check-circle"></i> Déjà prolongé
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="<c:url value='/prolongements/demande/${pret.id}'/>" method="post" style="display:inline;">
                                                            <button type="submit" class="btn btn-sm btn-primary">
                                                                <i class="fas fa-clock"></i> Demander
                                                            </button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <i class="fas fa-book"></i>
                            <p>Vous n'avez aucun prêt en cours.</p>
                            <a href="<c:url value='/livres'/>" class="btn btn-primary">
                                <i class="fas fa-search"></i> Parcourir le catalogue
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="info-box" style="margin-top: 20px;">
                <h3><i class="fas fa-info-circle"></i> À propos des prolongements</h3>
                <ul>
                    <li>Un seul prolongement est autorisé par prêt.</li>
                    <li>La demande de prolongement doit être validée par un bibliothécaire.</li>
                    <li>La durée du prolongement est égale à la durée de prêt initiale.</li>
                    <li>Vous pouvez annuler votre demande tant qu'elle n'a pas été traitée.</li>
                </ul>
            </div>
        </main>
    </div>
    
    <style>
        .status-badge {
            display: inline-flex;
            align-items: center;
            padding: 5px 8px;
            border-radius: 4px;
            font-size: 13px;
            font-weight: 500;
        }
        
        .status-badge i {
            margin-right: 5px;
        }
        
        .status-badge.pending {
            background-color: #fff8e1;
            color: #ff9800;
        }
        
        .status-badge.success {
            background-color: #e8f5e9;
            color: #4caf50;
        }
        
        .status-badge.danger {
            background-color: #ffebee;
            color: #f44336;
        }
        
        .status-badge.neutral {
            background-color: #f5f5f5;
            color: #757575;
        }
        
        .disabled-action {
            display: inline-flex;
            align-items: center;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 13px;
            background-color: #f5f5f5;
            color: #9e9e9e;
        }
        
        .disabled-action i {
            margin-right: 5px;
        }
        
        .info-box {
            background-color: #e3f2fd;
            border-radius: 8px;
            padding: 20px;
            margin-top: 30px;
        }
        
        .info-box h3 {
            color: #1976d2;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }
        
        .info-box h3 i {
            margin-right: 10px;
        }
        
        .info-box ul {
            margin-left: 30px;
            color: #555;
        }
        
        .info-box li {
            margin-bottom: 8px;
        }
    </style>
</body>
</html>
