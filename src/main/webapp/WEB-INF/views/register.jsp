<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-wrapper register-wrapper">
            <div class="auth-header">
                <h1><i class="fas fa-book-open"></i> Bibliothèque</h1>
                <p>Créez votre compte adhérent</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <div class="auth-form">
                <form action="<c:url value='/register'/>" method="post">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="nom">Nom</label>
                            <div class="input-icon">
                                <i class="fas fa-user"></i>
                                <input type="text" id="nom" name="nom" required placeholder="Votre nom">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="prenom">Prénom</label>
                            <div class="input-icon">
                                <i class="fas fa-user"></i>
                                <input type="text" id="prenom" name="prenom" required placeholder="Votre prénom">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="adresse">Adresse</label>
                        <div class="input-icon">
                            <i class="fas fa-home"></i>
                            <input type="text" id="adresse" name="adresse" required placeholder="Votre adresse">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="dateNaissance">Date de naissance</label>
                        <div class="input-icon">
                            <i class="fas fa-calendar"></i>
                            <input type="date" id="dateNaissance" name="dateNaissance" required>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="typeAdherentId">Type d'adhérent</label>
                        <div class="input-icon">
                            <i class="fas fa-users"></i>
                            <select id="typeAdherentId" name="typeAdherentId" required>
                                <option value="">-- Sélectionnez un type --</option>
                                <c:forEach items="${typeAdherents}" var="typeAdherent">
                                    <option value="${typeAdherent.id}">${typeAdherent.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="motDePasse">Mot de passe</label>
                            <div class="input-icon">
                                <i class="fas fa-lock"></i>
                                <input type="password" id="motDePasse" name="motDePasse" required placeholder="Votre mot de passe">
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="confirmMotDePasse">Confirmer le mot de passe</label>
                            <div class="input-icon">
                                <i class="fas fa-lock"></i>
                                <input type="password" id="confirmMotDePasse" name="confirmMotDePasse" required placeholder="Confirmez votre mot de passe">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-user-plus"></i> S'inscrire
                        </button>
                    </div>
                </form>
                
                <div class="auth-footer">
                    <p>Vous avez déjà un compte ? <a href="<c:url value='/login'/>">Se connecter</a></p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
