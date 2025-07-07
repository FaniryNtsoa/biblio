<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle réservation - Bibliothèque</title>
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
                <h1>Nouvelle réservation</h1>
                <div class="header-actions">
                    <a href="<c:url value='/livres/${livre.id}'/>" class="btn btn-icon">
                        <i class="fas fa-arrow-left"></i> Retour au livre
                    </a>
                </div>
            </header>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <div class="membership-container">
                <div class="membership-info">
                    <div class="membership-icon">
                        <i class="fas fa-calendar-alt"></i>
                    </div>
                    <div class="membership-details">
                        <h2>Demande de réservation</h2>
                        <p>Vous souhaitez réserver le livre suivant:</p>
                        <div class="book-details">
                            <h3>${livre.titre}</h3>
                            <p><strong>Auteur:</strong> ${livre.auteur}</p>
                            <p><strong>Édition:</strong> ${livre.edition}</p>
                        </div>
                    </div>
                </div>
                
                <div class="membership-form">
                    <h3>Informations de réservation</h3>
                    <form action="<c:url value='/reservations/nouveau'/>" method="post">
                        <input type="hidden" name="livreId" value="${livre.id}">
                        
                        <div class="form-group">
                            <label for="dateReservation">Date de début souhaitée</label>
                            <div class="input-date">
                                <i class="fas fa-calendar-alt"></i>
                                <input type="date" id="dateReservation" name="dateReservation" required 
                                        value="${minDate}">
                            </div>
                            <p class="form-help">Sélectionnez la date à laquelle vous souhaitez commencer votre prêt</p>
                        </div>
                        
                        <div class="info-box" style="background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0;">
                            <h4 style="color: #856404; margin-bottom: 10px;">
                                <i class="fas fa-info-circle"></i> Information importante
                            </h4>
                            <p style="color: #856404;">
                                Votre demande sera examinée par un bibliothécaire. Vous recevrez une notification lorsqu'elle sera traitée.
                            </p>
                            <p style="color: #856404; margin-top: 10px;">
                                Si la date souhaitée est atteinte et que votre réservation n'a pas été traitée, elle sera automatiquement annulée.
                            </p>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-calendar-plus"></i> Confirmer la réservation
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
