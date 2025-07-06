<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouveau prêt - Bibliothèque</title>
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
                <h1>Nouveau prêt</h1>
                <div class="header-actions">
                    <a href="<c:url value='/prets'/>" class="btn btn-icon">
                        <i class="fas fa-arrow-left"></i> Retour aux prêts
                    </a>
                </div>
            </header>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty warning}">
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-triangle"></i> ${warning}
                </div>
            </c:if>
            
            <c:if test="${hasPenalites}">
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-triangle"></i> ${warning}
                </div>
                <div class="date-warning-info" style="margin-bottom: 15px; padding: 10px; border-left: 4px solid #ffc107; background-color: #fff8e1;">
                    <h4 style="color: #ff9800; margin-bottom: 10px;">
                        <i class="fas fa-calendar-times"></i> Dates à éviter
                    </h4>
                    <p>Vous avez une pénalité active jusqu'au <strong>${dateFinPenalite}</strong>.</p>
                    <p>Veuillez choisir une date de prêt <strong>après cette date</strong> pour que votre demande soit acceptée.</p>
                </div>
            </c:if>
            
            <div class="membership-container">
                <div class="membership-info">
                    <div class="membership-icon">
                        <i class="fas fa-book-open"></i>
                    </div>
                    <div class="membership-details">
                        <h2>Demande de prêt de livre</h2>
                        <p>Sélectionnez un livre et le type de prêt souhaité.</p>
                        <p><strong>Note :</strong> La durée de prêt dépend du type sélectionné.</p>
                    </div>
                </div>
                
                <div class="membership-form">
                    <h3>Informations du prêt</h3>
                    <form action="<c:url value='/prets/nouveau'/>" method="post">
                        <div class="membership-period">
                            <div class="form-group">
                                <label for="datePret">Date de prêt</label>
                                <div class="input-date">
                                    <i class="fas fa-calendar-alt"></i>
                                    <input type="date" id="datePret" name="datePret" required 
                                           min="${java.time.LocalDate.now()}" 
                                           value="${java.time.LocalDate.now()}">
                                </div>
                                <p class="form-help">Sélectionnez la date de début du prêt</p>
                            </div>
                            
                            <div class="form-group">
                                <label for="livreId">Livre à emprunter</label>
                                <select id="livreId" name="livreId" required class="filter-select">
                                    <option value="">-- Sélectionnez un livre --</option>
                                    <c:forEach items="${livresDisponibles}" var="livre">
                                        <option value="${livre.id}" 
                                                ${selectedLivre != null && selectedLivre.id == livre.id ? 'selected' : ''}>
                                            ${livre.titre} - ${livre.auteur}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <c:if test="${selectedLivre != null}">
                                <div class="selected-book-info" style="background-color: #f5f8ff; border-radius: 8px; padding: 15px; margin: 15px 0;">
                                    <h4 style="color: #3949ab; margin-bottom: 10px;">
                                        <i class="fas fa-book"></i> Livre sélectionné
                                    </h4>
                                    <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 10px;">
                                        <strong>Titre:</strong> <span>${selectedLivre.titre}</span>
                                        <strong>Auteur:</strong> <span>${selectedLivre.auteur}</span>
                                        <strong>Année:</strong> <span>${selectedLivre.dateSortie.year}</span>
                                        <strong>Langue:</strong> <span>${selectedLivre.langue}</span>
                                    </div>
                                </div>
                            </c:if>
                            
                            <div class="form-group">
                                <label for="typePretId">Type de prêt</label>
                                <select id="typePretId" name="typePretId" required class="filter-select">
                                    <option value="">-- Sélectionnez le type de prêt --</option>
                                    <c:forEach items="${typesPret}" var="typePret">
                                        <option value="${typePret.id}" data-sur-place="${typePret.surPlace}">
                                            ${typePret.nom}
                                            <c:if test="${typePret.surPlace}"> (sur place)</c:if>
                                        </option>
                                    </c:forEach>
                                </select>
                                <p class="form-help">Choisissez entre consultation sur place ou emprunt à domicile</p>
                            </div>
                            
                            <div class="info-box" id="infoBox" style="background-color: #e8f5e9; border-left: 4px solid #4caf50; padding: 15px; margin: 20px 0;">
                                <h4 style="color: #2e7d32; margin-bottom: 10px;">
                                    <i class="fas fa-info-circle"></i> Informations importantes
                                </h4>
                                <ul id="infoList" style="margin-left: 20px; color: #2e7d32;">
                                    <li>Durée maximale de prêt pour votre type d'adhérent: <strong>${dureePretMax} jours</strong></li>
                                    <li>Nombre maximum de prêts simultanés: <strong>${nombrePretMax} livre(s)</strong></li>
                                    <li>Veuillez sélectionner un type de prêt pour voir les conditions spécifiques</li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-check"></i> Confirmer la demande de prêt
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const livreSelect = document.getElementById('livreId');
            const typePretSelect = document.getElementById('typePretId');
            const infoList = document.getElementById('infoList');
            
            livreSelect.addEventListener('change', function() {
                if (this.value) {
                    // Recharger la page avec le livre sélectionné
                    window.location.href = '<c:url value="/prets/nouveau"/>?livreId=' + this.value;
                }
            });
            
            typePretSelect.addEventListener('change', function() {
                const selectedOption = this.options[this.selectedIndex];
                const surPlace = selectedOption.getAttribute('data-sur-place') === 'true';
                
                if (this.value) {
                    let infoHTML = `
                        <li>Durée maximale de prêt pour votre type d'adhérent: <strong>${dureePretMax} jours</strong></li>
                        <li>Nombre maximum de prêts simultanés: <strong>${nombrePretMax} livre(s)</strong></li>
                    `;
                    if (surPlace) {
                        infoHTML += `
                            <li>Type: <strong>Consultation sur place</strong></li>
                            <li>Le livre doit être consulté dans la bibliothèque</li>
                            <li>Aucune limite de nombre de consultations</li>
                            <li>Le prêt sera automatiquement marqué comme terminé</li>
                        `;
                    } else {
                        infoHTML += `
                            <li>Type: <strong>Emprunt à domicile</strong></li>
                            <li>Vous pourrez emporter le livre chez vous</li>
                            <li>Durée: ${dureePretMax} jours maximum</li>
                            <li>Respectez la date de retour pour éviter les pénalités</li>
                            <li>Possibilité de prolongation selon les règles</li>
                        `;
                    }
                    infoList.innerHTML = infoHTML;
                } else {
                    infoList.innerHTML = `
                        <li>Durée maximale de prêt pour votre type d'adhérent: <strong>${dureePretMax} jours</strong></li>
                        <li>Nombre maximum de prêts simultanés: <strong>${nombrePretMax} livre(s)</strong></li>
                        <li>Veuillez sélectionner un type de prêt pour voir les conditions spécifiques</li>
                    `;
                }
            });
        });
    </script>
</body>
</html>
</html>
