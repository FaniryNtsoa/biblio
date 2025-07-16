<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Jours fériés - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <c:set var="currentPage" value="jours-feries" scope="request" />
        <jsp:include page="../fragments/sidebar.jsp" />
        
        <main class="content">
            <header class="content-header">
                <h1>Jours fériés et dimanches</h1>
                <div class="header-actions">
                    <a href="<c:url value='/home'/>" class="btn btn-icon">
                        <i class="fas fa-arrow-left"></i> Retour à l'accueil
                    </a>
                </div>
            </header>
            
            <div class="info-box" style="margin-bottom: 20px; padding: 20px; background-color: #e3f2fd; border-radius: 8px;">
                <h3 style="margin-top: 0;"><i class="fas fa-info-circle"></i> Information importante</h3>
                <p>Veuillez noter que la bibliothèque est fermée tous les dimanches et jours fériés. Aucun emprunt ni retour de livre ne peut être effectué pendant ces jours.</p>
            </div>
            
            <div class="card" style="margin-bottom: 20px;">
                <h2>Jours fériés à venir</h2>
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Jour férié</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${joursFeries}" var="jourFerie">
                                <tr>
                                    <td><fmt:formatDate value="${jourFerie.date}" pattern="dd/MM/yyyy" /></td>
                                    <td>${jourFerie.nom}</td>
                                    <td>${jourFerie.description}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <div class="card">
                <h2>Heures d'ouverture</h2>
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Jour</th>
                                <th>Horaires</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Lundi - Vendredi</td>
                                <td>08:00 - 18:00</td>
                            </tr>
                            <tr>
                                <td>Samedi</td>
                                <td>09:00 - 16:00</td>
                            </tr>
                            <tr>
                                <td>Dimanche</td>
                                <td><strong>Fermé</strong></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
