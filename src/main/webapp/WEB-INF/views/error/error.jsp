<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .error-container {
            text-align: center;
            padding: 50px 20px;
            max-width: 800px;
            margin: 0 auto;
        }
        .error-icon {
            font-size: 72px;
            color: #d32f2f;
            margin-bottom: 30px;
        }
        .error-title {
            font-size: 28px;
            margin-bottom: 20px;
            color: #333;
        }
        .error-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
        }
        .error-actions {
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <div class="app-container">
        <main class="content">
            <div class="error-container">
                <div class="error-icon">
                    <i class="fas fa-exclamation-circle"></i>
                </div>
                <h1 class="error-title">Une erreur est survenue</h1>
                <p class="error-message">
                    Nous sommes désolés, mais une erreur inattendue s'est produite.
                    <br>
                    L'équipe technique a été informée du problème.
                </p>
                <div class="error-actions">
                    <a href="<c:url value='/home'/>" class="btn btn-primary">
                        <i class="fas fa-home"></i> Retourner à l'accueil
                    </a>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
