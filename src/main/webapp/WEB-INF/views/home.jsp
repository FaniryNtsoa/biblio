<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil - Bibliothèque</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <div class="app-container">
        <nav class="sidebar">
            <div class="sidebar-header">
                <h2><i class="fas fa-book-open"></i> Bibliothèque</h2>
            </div>
            <div class="sidebar-user">
                <div class="user-avatar">
                    <i class="fas fa-user-circle"></i>
                </div>
                <div class="user-info">
                    <h3>${adherent.prenom} ${adherent.nom}</h3>
                    <p>${adherent.typeAdherent.nom}</p>
                </div>
            </div>
            <ul class="sidebar-menu">
                <li class="active"><a href="#"><i class="fas fa-home"></i> Accueil</a></li>
                <li><a href="#"><i class="fas fa-search"></i> Rechercher</a></li>
                <li><a href="#"><i class="fas fa-book"></i> Mes emprunts</a></li>
                <li><a href="#"><i class="fas fa-clock"></i> Mes réservations</a></li>
                <li><a href="#"><i class="fas fa-history"></i> Historique</a></li>
                <li><a href="#"><i class="fas fa-cog"></i> Paramètres</a></li>
                <li><a href="<c:url value='/logout'/>"><i class="fas fa-sign-out-alt"></i> Déconnexion</a></li>
            </ul>
        </nav>
        
        <main class="content">
            <header class="content-header">
                <h1>Bienvenue, ${adherent.prenom} !</h1>
                <div class="header-actions">
                    <div class="search-bar">
                        <input type="text" placeholder="Rechercher un livre...">
                        <button><i class="fas fa-search"></i></button>
                    </div>
                    <div class="notifications">
                        <i class="fas fa-bell"></i>
                    </div>
                </div>
            </header>
            
            <div class="dashboard">
                <div class="dashboard-cards">
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-book"></i></div>
                        <div class="card-info">
                            <h3>Mes emprunts</h3>
                            <p>0 livre(s) emprunté(s)</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-clock"></i></div>
                        <div class="card-info">
                            <h3>Mes réservations</h3>
                            <p>0 réservation(s) en cours</p>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-icon"><i class="fas fa-exclamation-circle"></i></div>
                        <div class="card-info">
                            <h3>Retards</h3>
                            <p>0 livre(s) en retard</p>
                        </div>
                    </div>
                </div>
                
                <div class="recent-books">
                    <h2>Nouveautés</h2>
                    <div class="book-list">
                        <p class="empty-state">Les nouveautés apparaîtront ici</p>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
