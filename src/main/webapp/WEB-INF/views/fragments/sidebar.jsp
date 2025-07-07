<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <li class="${currentPage == 'home' ? 'active' : ''}">
            <a href="<c:url value='/home'/>"><i class="fas fa-home"></i> Accueil</a>
        </li>
        <li class="${currentPage == 'livres' ? 'active' : ''}">
            <a href="<c:url value='/livres'/>"><i class="fas fa-book"></i> Catalogue</a>
        </li>
        <li class="${currentPage == 'inscription' ? 'active' : ''}">
            <a href="<c:url value='/inscription'/>">
                <i class="fas fa-user-plus"></i> Adhésion
                <c:if test="${not isActiveMember}">
                    <span class="menu-alert"><i class="fas fa-exclamation-circle"></i></span>
                </c:if>
            </a>
        </li>
        <li class="${currentPage == 'prets' ? 'active' : ''}">
            <c:choose>
                <c:when test="${isActiveMember}">
                    <a href="<c:url value='/prets'/>"><i class="fas fa-bookmark"></i> Mes emprunts</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/inscription'/>" onclick="showMembershipAlert(event)">
                        <i class="fas fa-bookmark"></i> Mes emprunts
                        <span class="menu-alert"><i class="fas fa-lock"></i></span>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
        <li class="${currentPage == 'reservations' ? 'active' : ''}">
            <c:choose>
                <c:when test="${isActiveMember}">
                    <a href="<c:url value='/reservations'/>"><i class="fas fa-calendar-alt"></i> Mes réservations</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/inscription'/>" onclick="showMembershipAlert(event)">
                        <i class="fas fa-calendar-alt"></i> Mes réservations
                        <span class="menu-alert"><i class="fas fa-lock"></i></span>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
        <li class="${currentPage == 'historique' ? 'active' : ''}">
            <c:choose>
                <c:when test="${isActiveMember}">
                    <a href="<c:url value='/historique'/>"><i class="fas fa-history"></i> Historique</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/inscription'/>" onclick="showMembershipAlert(event)">
                        <i class="fas fa-history"></i> Historique
                        <span class="menu-alert"><i class="fas fa-lock"></i></span>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
        <li class="${currentPage == 'profil' ? 'active' : ''}">
            <a href="#"><i class="fas fa-user-cog"></i> Mon profil</a>
        </li>
        <li>
            <a href="<c:url value='/logout'/>"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
        </li>
    </ul>
</nav>

<style>
.menu-alert {
    display: inline-block;
    margin-left: 8px;
    color: #ff9800;
    animation: pulse 1.5s infinite;
}

@keyframes pulse {
    0% { opacity: 0.5; }
    50% { opacity: 1; }
    100% { opacity: 0.5; }
}
</style>

<script>
function showMembershipAlert(event) {
    event.preventDefault();
    if (confirm('Cette fonctionnalité nécessite une adhésion active. Souhaitez-vous devenir membre maintenant ?')) {
        window.location.href = '<c:url value="/inscription"/>';
    }
}
</script>
