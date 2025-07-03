<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-wrapper">
            <div class="auth-header">
                <h1><i class="fas fa-book-open"></i> Bibliothèque</h1>
                <p>Accédez à votre espace adhérent</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            
            <div class="auth-form">
                <form action="<c:url value='/login'/>" method="post">
                    <div class="form-group">
                        <label for="nom">Nom</label>
                        <div class="input-icon">
                            <i class="fas fa-user"></i>
                            <input type="text" id="nom" name="nom" required placeholder="Votre nom">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="motDePasse">Mot de passe</label>
                        <div class="input-icon">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="motDePasse" name="motDePasse" required placeholder="Votre mot de passe">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-sign-in-alt"></i> Se connecter
                        </button>
                    </div>
                </form>
                
                <div class="auth-footer">
                    <p>Vous n'avez pas de compte ? <a href="<c:url value='/register'/>">S'inscrire</a></p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
