<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Bibliothécaire - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-wrapper">
            <div class="auth-header">
                <h1><i class="fas fa-user-tie"></i> Espace Bibliothécaire</h1>
                <p>Connectez-vous pour gérer les réservations et les prêts</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <div class="auth-form">
                <form action="<c:url value='/bibliothecaire/login'/>" method="post">
                    <div class="form-group">
                        <label for="username">Identifiant</label>
                        <div class="input-icon">
                            <i class="fas fa-user"></i>
                            <input type="text" id="username" name="username" required placeholder="Votre identifiant">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Mot de passe</label>
                        <div class="input-icon">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="password" name="password" required placeholder="Votre mot de passe">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-sign-in-alt"></i> Se connecter
                        </button>
                    </div>
                </form>
                
                <div class="auth-footer">
                    <a href="<c:url value='/login'/>" class="back-link">
                        <i class="fas fa-arrow-left"></i> Retour à l'espace adhérent
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <style>
        .back-link {
            display: inline-flex;
            align-items: center;
            color: #3949ab;
            font-size: 14px;
            transition: all 0.3s;
        }
        
        .back-link i {
            margin-right: 8px;
        }
        
        .back-link:hover {
            color: #1a237e;
            text-decoration: underline;
        }
        
        .auth-wrapper {
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }
        
        .auth-header h1 {
            color: #1a237e;
        }
    </style>
</body>
</html>
