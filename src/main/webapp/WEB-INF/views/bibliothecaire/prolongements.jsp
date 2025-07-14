<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des prolongements - Bibliothécaire</title>
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
                    <li>
                        <a href="<c:url value='/bibliothecaire/dashboard'/>">
                            <i class="fas fa-tachometer-alt"></i> <span>Tableau de bord</span>
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value='/bibliothecaire/prets'/>">
                            <i class="fas fa-book-open"></i> <span>Gestion des prêts</span>
                        </a>
                    </li>
                    <li class="active">
                        <a href="<c:url value='/bibliothecaire/prolongements'/>">
                            <i class="fas fa-hourglass-half"></i> <span>Prolongements</span>
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
                <h1><i class="fas fa-hourglass-half"></i> Gestion des prolongements</h1>
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
                <h2><i class="fas fa-clock"></i> Demandes de prolongement en attente (${prolongementsEnAttente.size()})</h2>
                
                <c:choose>
                    <c:when test="${not empty prolongementsEnAttente}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Adhérent</th>
                                        <th>Livre</th>
                                        <th>Date du prêt</th>
                                        <th>Date de retour actuelle</th>
                                        <th>Nouvelle date si accepté</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="prolongement" items="${prolongementsEnAttente}">
                                        <tr>
                                            <td>${prolongement.id}</td>
                                            <td>${prolongement.pret.adherent.prenom} ${prolongement.pret.adherent.nom}</td>
                                            <td>${prolongement.pret.exemplaire.livre.titre}</td>
                                            <td>
                                                <fmt:parseDate value="${prolongement.pret.datePret}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td>
                                                <fmt:parseDate value="${prolongement.pret.dateRetourPrevue}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td>
                                                <jsp:useBean id="dateCalculator" class="java.util.Date" />
                                                <c:set var="millisecondsPerDay" value="86400000" />
                                                <fmt:parseDate value="${prolongement.pret.dateRetourPrevue}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                <c:set var="newDateMillis" value="${parsedDate.time + (prolongement.nbJour * millisecondsPerDay)}" />
                                                <jsp:setProperty name="dateCalculator" property="time" value="${newDateMillis}" />
                                                <fmt:formatDate value="${dateCalculator}" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td class="actions">
                                                <form action="<c:url value='/bibliothecaire/prolongements/${prolongement.id}/accept'/>" method="post" style="display:inline;">
                                                    <button type="submit" class="btn btn-sm btn-success">
                                                        <i class="fas fa-check"></i> Accepter
                                                    </button>
                                                </form>
                                                <form action="<c:url value='/bibliothecaire/prolongements/${prolongement.id}/reject'/>" method="post" style="display:inline;">
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
                            <p>Aucune demande de prolongement en attente</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="info-box" style="margin-top: 20px;">
                <h3><i class="fas fa-info-circle"></i> Règles de prolongement</h3>
                <ul>
                    <li>Un seul prolongement est autorisé par prêt.</li>
                    <li>La durée du prolongement est égale à la durée de prêt standard pour l'adhérent concerné.</li>
                    <li>Lorsqu'un prolongement est accepté, la date de retour prévue du prêt est automatiquement mise à jour.</li>
                    <li>Les pénalités de retard sont calculées à partir de la nouvelle date de retour prévue.</li>
                </ul>
            </div>
        </main>
    </div>
    
    <style>
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
