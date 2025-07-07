<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes réservations - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="reservations" scope="request" />
        <jsp:include page="../fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <h1>Mes réservations</h1>
                <div class="header-actions">
                    <a href="<c:url value='/livres'/>" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Parcourir le catalogue
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
            
            <div class="reservations-container">
                <!-- Réservations en attente -->
                <div class="reservation-section">
                    <h2><i class="fas fa-clock"></i> En attente de validation (${reservationsEnAttente.size()})</h2>
                    
                    <c:choose>
                        <c:when test="${not empty reservationsEnAttente}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date de réservation</th>
                                            <th>Date de demande</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="reservation" items="${reservationsEnAttente}">
                                            <tr>
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
                                                    <c:if test="${reservationService.isReservationEnAttente(reservation)}">
                                                        <form action="<c:url value='/reservations/${reservation.id}/annuler'/>" method="post" style="display:inline;">
                                                            <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')">
                                                                <i class="fas fa-times"></i> Annuler
                                                            </button>
                                                        </form>
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
                                <i class="fas fa-info-circle"></i>
                                <p>Aucune réservation en attente</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Réservations confirmées -->
                <div class="reservation-section">
                    <h2><i class="fas fa-check-circle"></i> Confirmées (${reservationsConfirmees.size()})</h2>
                    
                    <c:choose>
                        <c:when test="${not empty reservationsConfirmees}">
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date de réservation</th>
                                            <th>Date de confirmation</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="reservation" items="${reservationsConfirmees}">
                                            <tr>
                                                <td>${reservation.livre.titre}</td>
                                                <td>
                                                    <fmt:parseDate value="${reservation.dateReservation}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                    <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <c:forEach var="historique" items="${reservation.historiqueStatusReservations}" varStatus="loop">
                                                        <c:if test="${loop.last}">
                                                            <fmt:parseDate value="${historique.dateReservation}" pattern="yyyy-MM-dd" var="parsedDateConfirm" type="date" />
                                                            <fmt:formatDate value="${parsedDateConfirm}" type="date" pattern="dd/MM/yyyy" />
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-info-circle"></i>
                                <p>Aucune réservation confirmée</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Historique des réservations (rejetées, annulées, expirées) -->
                <div class="reservation-section">
                    <h2><i class="fas fa-history"></i> Historique</h2>
                    
                    <c:if test="${empty reservationsRejetees and empty reservationsAnnulees and empty reservationsExpirees}">
                        <div class="empty-state">
                            <i class="fas fa-info-circle"></i>
                            <p>Aucun historique de réservation</p>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty reservationsRejetees}">
                        <div class="subsection">
                            <h3>Réservations rejetées (${reservationsRejetees.size()})</h3>
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date demandée</th>
                                            <th>Date de rejet</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="reservation" items="${reservationsRejetees}">
                                            <tr>
                                                <td>${reservation.livre.titre}</td>
                                                <td>
                                                    <fmt:parseDate value="${reservation.dateReservation}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                    <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <c:forEach var="historique" items="${reservation.historiqueStatusReservations}" varStatus="loop">
                                                        <c:if test="${loop.last}">
                                                            <fmt:parseDate value="${historique.dateReservation}" pattern="yyyy-MM-dd" var="parsedDateRejet" type="date" />
                                                            <fmt:formatDate value="${parsedDateRejet}" type="date" pattern="dd/MM/yyyy" />
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty reservationsAnnulees}">
                        <div class="subsection">
                            <h3>Réservations annulées (${reservationsAnnulees.size()})</h3>
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date demandée</th>
                                            <th>Date d'annulation</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="reservation" items="${reservationsAnnulees}">
                                            <tr>
                                                <td>${reservation.livre.titre}</td>
                                                <td>
                                                    <fmt:parseDate value="${reservation.dateReservation}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                    <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <c:forEach var="historique" items="${reservation.historiqueStatusReservations}" varStatus="loop">
                                                        <c:if test="${loop.last}">
                                                            <fmt:parseDate value="${historique.dateReservation}" pattern="yyyy-MM-dd" var="parsedDateAnnul" type="date" />
                                                            <fmt:formatDate value="${parsedDateAnnul}" type="date" pattern="dd/MM/yyyy" />
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty reservationsExpirees}">
                        <div class="subsection">
                            <h3>Réservations expirées (${reservationsExpirees.size()})</h3>
                            <div class="table-responsive">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Livre</th>
                                            <th>Date demandée</th>
                                            <th>Date d'expiration</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="reservation" items="${reservationsExpirees}">
                                            <tr>
                                                <td>${reservation.livre.titre}</td>
                                                <td>
                                                    <fmt:parseDate value="${reservation.dateReservation}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                    <fmt:formatDate value="${parsedDate}" type="date" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <c:forEach var="historique" items="${reservation.historiqueStatusReservations}" varStatus="loop">
                                                        <c:if test="${loop.last}">
                                                            <fmt:parseDate value="${historique.dateReservation}" pattern="yyyy-MM-dd" var="parsedDateExp" type="date" />
                                                            <fmt:formatDate value="${parsedDateExp}" type="date" pattern="dd/MM/yyyy" />
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </main>
    </div>
    
    <style>
        .reservations-container {
            display: flex;
            flex-direction: column;
            gap: 30px;
        }
        
        .reservation-section {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            padding: 20px;
        }
        
        .reservation-section h2 {
            font-size: 18px;
            margin-bottom: 20px;
            color: #333;
            display: flex;
            align-items: center;
        }
        
        .reservation-section h2 i {
            margin-right: 10px;
            color: #3949ab;
        }
        
        .subsection {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .subsection h3 {
            font-size: 16px;
            margin-bottom: 15px;
            color: #555;
        }
        
        .empty-state {
            padding: 20px;
            text-align: center;
            color: #999;
        }
        
        .empty-state i {
            font-size: 24px;
            margin-bottom: 10px;
            color: #bbb;
        }
    </style>
</body>
</html>
