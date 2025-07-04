-- Insertion des états d'exemplaire
INSERT INTO etat_exemplaire (nom) VALUES
('Excellent'),
('Bon'),
('Moyen'),
('Usé'),
('Détérioré');

-- Insertion des catégories
INSERT INTO categorie (nom) VALUES
('Fantasy'),
('Science-Fiction'),
('Dystopie'),
('Jeunesse'),
('Conte'),
('Classique'),
('Policier'),
('Philosophie'),
('Bande Dessinée'),
('Aventure');

-- Suppression des anciens types de prêt avec durée
DELETE FROM type_pret WHERE nom IN ('Standard', 'Prolongé', 'Étudiant', 'Enseignant', 'Recherche');

-- Insertion des nouveaux types de prêt corrects
INSERT INTO type_pret (nom, sur_place) VALUES 
('a domicile', false), 
('sur place', true);

-- Insertion des livres (déjà fournie)
INSERT INTO livre (titre, auteur, edition, isbn, date_sortie, resume, langue, age_min) VALUES
('Le Seigneur des Anneaux', 'J.R.R. Tolkien', 'Christian Bourgois', '9782266282367', '1954-07-29', 'Une quete epique pour detruire un anneau malefique.', 'Francais', 12),
('1984', 'George Orwell', 'Gallimard', '9782070368226', '1949-06-08', 'Une dystopie sur un regime totalitaire.', 'Francais', 16),
('Harry Potter a l''ecole des sorciers', 'J.K. Rowling', 'Gallimard Jeunesse', '9782070518424', '1997-06-26', 'Un jeune sorcier decouvre son destin.', 'Francais', 8),
('Le Petit Prince', 'Antoine de Saint-Exupery', 'Gallimard', '9782070408504', '1943-04-06', 'Un conte philosophique pour petits et grands.', 'Francais', 6),
('Dune', 'Frank Herbert', 'Robert Laffont', '9782221117535', '1965-08-01', 'Une saga de science-fiction sur une planete desertique.', 'Francais', 14),
('Les Miserables', 'Victor Hugo', 'Pocket', '9782266196830', '1862-01-01', 'L''histoire de Jean Valjean et de sa redemption.', 'Francais', 12),
('Le Crime de l''Orient-Express', 'Agatha Christie', 'Le Masque', '9782702435347', '1934-01-01', 'Hercule Poirot enquete sur un meurtre dans un train.', 'Francais', 10),
('L''Etranger', 'Albert Camus', 'Gallimard', '9782070360022', '1942-05-19', 'Un homme indifferent a son propre destin.', 'Francais', 14),
('Asterix le Gaulois', 'Goscinny et Uderzo', 'Dargaud', '9782012101339', '1961-10-29', 'Les aventures d''un petit gaulois malicieux.', 'Francais', 6),
('Le Vieil Homme et la Mer', 'Ernest Hemingway', 'Gallimard', '9782070368233', '1952-09-01', 'Un vieux pecheur affronte un enorme marlin.', 'Francais', 10);

-- Association des livres aux catégories
INSERT INTO livre_categorie (livre_id, categorie_id) VALUES
-- Le Seigneur des Anneaux (Fantasy, Aventure)
(1, 1), (1, 10),
-- 1984 (Science-Fiction, Dystopie)
(2, 2), (2, 3),
-- Harry Potter (Fantasy, Jeunesse)
(3, 1), (3, 4),
-- Le Petit Prince (Conte, Jeunesse, Philosophie)
(4, 5), (4, 4), (4, 8),
-- Dune (Science-Fiction, Aventure)
(5, 2), (5, 10),
-- Les Miserables (Classique)
(6, 6),
-- Le Crime de l'Orient-Express (Policier)
(7, 7),
-- L'Etranger (Classique, Philosophie)
(8, 6), (8, 8),
-- Asterix le Gaulois (Bande Dessinée, Jeunesse)
(9, 9), (9, 4),
-- Le Vieil Homme et la Mer (Classique, Aventure)
(10, 6), (10, 10);

-- Insertion des exemplaires pour chaque livre
-- Le Seigneur des Anneaux (3 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(1, 1), -- Excellent
(1, 2), -- Bon
(1, 2); -- Bon

-- 1984 (4 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(2, 1), -- Excellent
(2, 1), -- Excellent
(2, 2), -- Bon
(2, 3); -- Moyen

-- Harry Potter à l'école des sorciers (5 exemplaires - populaire)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(3, 1), -- Excellent
(3, 1), -- Excellent
(3, 2), -- Bon
(3, 2), -- Bon
(3, 3); -- Moyen

-- Le Petit Prince (6 exemplaires - très populaire)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(4, 1), -- Excellent
(4, 1), -- Excellent
(4, 2), -- Bon
(4, 2), -- Bon
(4, 3), -- Moyen
(4, 3); -- Moyen

-- Dune (2 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(5, 1), -- Excellent
(5, 2); -- Bon

-- Les Miserables (3 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(6, 2), -- Bon
(6, 3), -- Moyen
(6, 4); -- Usé

-- Le Crime de l'Orient-Express (3 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(7, 1), -- Excellent
(7, 2), -- Bon
(7, 2); -- Bon

-- L'Etranger (2 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(8, 2), -- Bon
(8, 3); -- Moyen

-- Asterix le Gaulois (4 exemplaires - populaire chez les jeunes)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(9, 1), -- Excellent
(9, 2), -- Bon
(9, 3), -- Moyen
(9, 4); -- Usé

-- Le Vieil Homme et la Mer (2 exemplaires)
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(10, 1), -- Excellent
(10, 2); -- Bon

-- Insertion de types d'adhérents
INSERT INTO type_adherent (nom) VALUES
('Étudiant'),
('Enseignant'),
('Particulier'),
('Senior'),
('Jeune');

-- Insertion d'adhérents de test
INSERT INTO adherent (type_adherent_id, nom, prenom, adresse, mot_de_passe, dtn) VALUES
(1, 'Dupont', 'Marie', '123 Rue de la Paix', 'password123', '1995-03-15'),
(2, 'Martin', 'Pierre', '456 Avenue des Sciences', 'prof2024', '1980-07-22'),
(3, 'Bernard', 'Sophie', '789 Boulevard Central', 'sophie456', '1988-11-30'),
(4, 'Leroy', 'Jean', '321 Rue du Commerce', 'jean789', '1965-09-12'),
(5, 'Moreau', 'Lucas', '654 Impasse des Fleurs', 'lucas321', '2005-12-08');

-- Quelques inscriptions actives
INSERT INTO inscription (adherent_id, date_inscription, date_expiration) VALUES
(1, '2024-01-15', '2024-07-15'),
(2, '2023-09-01', '2024-09-01'),
(3, '2024-02-01', '2024-05-01'),
(4, '2024-03-01', '2024-12-01'),
(5, '2024-01-10', '2024-04-10');

-- Insertion des règles de gestion par type d'adhérent
INSERT INTO gestion_adherent (type_adherent_id, duree_pret, nombre_pret_max) VALUES
(1, 21, 3), -- Étudiant: 21 jours, 3 livres max
(2, 30, 5), -- Enseignant: 30 jours, 5 livres max
(3, 14, 2), -- Particulier: 14 jours, 2 livres max
(4, 21, 4), -- Senior: 21 jours, 4 livres max
(5, 14, 2); -- Jeune: 14 jours, 2 livres max

-- Insertion des statuts de prêt
INSERT INTO status_pret (nom) VALUES
('EN_COURS'),
('RENDU'),
('EN_RETARD'),
('PROLONGE');

-- Quelques prêts de test avec les nouvelles règles
INSERT INTO pret (adherent_id, exemplaire_id, type_pret_id, date_pret, date_retour_prevue) VALUES
-- Marie (Étudiante) emprunte Le Seigneur des Anneaux à domicile
(1, 1, 1, '2024-01-15', '2024-02-05'), -- 21 jours selon règle étudiant
-- Pierre (Enseignant) consultation sur place de 1984
(2, 5, 2, '2024-01-10', NULL), -- Sur place, pas de date de retour
-- Sophie (Particulier) emprunte Harry Potter
(3, 11, 1, '2024-01-20', '2024-02-03'), -- 14 jours selon règle particulier
-- Jean (Senior) emprunte Le Crime de l'Orient-Express
(4, 21, 1, '2024-01-18', '2024-02-08'), -- 21 jours selon règle senior
-- Lucas (Jeune) consultation sur place du Petit Prince
(5, 16, 2, '2024-01-22', NULL); -- Sur place

-- Historique des prêts avec les nouvelles colonnes
INSERT INTO historique_pret (status_pret_id, pret_id, exemplaire_id, date_changement, commentaire) VALUES
-- Prêt en cours de Marie
(1, 1, 1, '2024-01-15', 'Prêt à domicile - Étudiant'),
-- Consultation sur place de Pierre (rendue immédiatement)
(1, 2, 5, '2024-01-10', 'Consultation sur place débutée'),
(2, 2, 5, '2024-01-10', 'Consultation terminée'),
-- Prêt en cours de Sophie
(1, 3, 11, '2024-01-20', 'Prêt à domicile - Particulier'),
-- Prêt en cours de Jean
(1, 4, 21, '2024-01-18', 'Prêt à domicile - Senior'),
-- Consultation sur place de Lucas (rendue immédiatement)
(1, 5, 16, '2024-01-22', 'Consultation sur place débutée'),
(2, 5, 16, '2024-01-22', 'Consultation terminée');

-- Ajout de quelques prêts en retard pour les tests
INSERT INTO pret (adherent_id, exemplaire_id, type_pret_id, date_pret, date_retour_prevue) VALUES
(1, 2, 1, '2023-12-01', '2023-12-22'); -- Prêt en retard de Marie

INSERT INTO historique_pret (status_pret_id, pret_id, exemplaire_id, date_changement, commentaire) VALUES
(1, 6, 2, '2023-12-01', 'Prêt à domicile'),
(3, 6, 2, '2023-12-23', 'Prêt en retard détecté automatiquement');

-- Insertion des statuts de réservation
INSERT INTO status_reservation (nom) VALUES
('EN_ATTENTE'),
('CONFIRMEE'),
('ANNULEE'),
('EXPIREE');

-- Quelques réservations pour tester la disponibilité
INSERT INTO reservation (adherent_id, livre_id, date_reservation, date_expiration) VALUES
(5, 1, '2024-01-25', '2024-02-01'), -- Lucas réserve Le Seigneur des Anneaux
(3, 2, '2024-01-22', '2024-01-29'); -- Sophie réserve 1984

-- Historique des réservations
INSERT INTO historique_status_reservation (status_reservation_id, reservation_id, date_changement) VALUES
(1, 1, '2024-01-25'), -- Lucas en attente
(1, 2, '2024-01-22'); -- Sophie en attente

-- Données de test pour valider les règles d'âge
-- Ajout d'un adhérent mineur pour tester la règle d'âge
INSERT INTO adherent (type_adherent_id, nom, prenom, adresse, mot_de_passe, dtn) VALUES
(5, 'Petit', 'Tom', '999 Rue des Enfants', 'tom123', '2018-05-10'); -- 5 ans

INSERT INTO inscription (adherent_id, date_inscription, date_expiration) VALUES
(6, '2024-01-01', '2024-12-31'); -- Inscription active pour Tom

-- Test de limite de prêts - donnons un deuxième prêt à Sophie (limite 2 pour particulier)
INSERT INTO pret (adherent_id, exemplaire_id, type_pret_id, date_pret, date_retour_prevue) VALUES
(3, 22, 1, '2024-01-25', '2024-02-08'); -- Sophie emprunte un deuxième livre

INSERT INTO historique_pret (status_pret_id, pret_id, exemplaire_id, date_changement, commentaire) VALUES
(1, 7, 22, '2024-01-25', 'Deuxième prêt de Sophie - limite atteinte');

-- Statuts de prolongement
INSERT INTO status_prolongement (nom) VALUES
(1), -- Demandé
(2), -- Accepté
(3); -- Refusé

-- Quelques prolongements pour tester
INSERT INTO prolongement (pret_id, nb_jour) VALUES
(1, 7), -- Marie demande 7 jours de prolongement
(4, 14); -- Jean demande 14 jours

-- Historique des prolongements
INSERT INTO historique_prolongement (status_prolongement_id, prolongement_id) VALUES
(2, 1), -- Prolongement de Marie accepté
(1, 2); -- Prolongement de Jean en attente

-- Quelques pénalités pour tester
INSERT INTO penalite (pret_id, description, date_penalite) VALUES
(6, 'Retard de restitution - 1 jour', '2023-12-23'),
(6, 'Retard de restitution - 5 jours', '2023-12-27');

-- Données supplémentaires pour tester les validations

-- Adhérent avec plusieurs prêts pour tester la limite
INSERT INTO adherent (type_adherent_id, nom, prenom, adresse, mot_de_passe, dtn) VALUES
(3, 'Test', 'Limite', '888 Rue des Tests', 'test123', '1990-01-01'); -- Particulier

INSERT INTO inscription (adherent_id, date_inscription, date_expiration) VALUES
(7, '2024-01-01', '2024-12-31'); -- Inscription active

-- Donnons-lui déjà 2 prêts (limite pour particulier)
INSERT INTO pret (adherent_id, exemplaire_id, type_pret_id, date_pret, date_retour_prevue) VALUES
(7, 3, 1, '2024-01-20', '2024-02-03'), -- Premier prêt
(7, 4, 1, '2024-01-22', '2024-02-05'); -- Deuxième prêt

INSERT INTO historique_pret (status_pret_id, pret_id, exemplaire_id, date_changement, commentaire) VALUES
(1, 8, 3, '2024-01-20', 'Premier prêt de Test Limite'),
(1, 9, 4, '2024-01-22', 'Deuxième prêt de Test Limite - limite atteinte');

-- Livre avec âge minimum élevé pour tester la validation d'âge
INSERT INTO livre (titre, auteur, edition, isbn, date_sortie, resume, langue, age_min) VALUES
('Manuel Universitaire Avancé', 'Dr. Expert', 'Éditions Scientifiques', '9780000000000', '2020-01-01', 'Manuel pour étudiants avancés uniquement.', 'Francais', 18);

-- Association à une catégorie
INSERT INTO livre_categorie (livre_id, categorie_id) VALUES
(11, 6); -- Classique

-- Exemplaires pour ce livre
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(11, 1), -- Excellent
(11, 2); -- Bon

-- Test avec un livre sans exemplaires disponibles
INSERT INTO livre (titre, auteur, edition, isbn, date_sortie, resume, langue, age_min) VALUES
('Livre Épuisé', 'Auteur Rare', 'Édition Limitée', '9780000000001', '2010-01-01', 'Livre sans exemplaires disponibles.', 'Francais', 10);

INSERT INTO livre_categorie (livre_id, categorie_id) VALUES
(12, 6); -- Classique

-- Pas d'exemplaires pour ce livre (pour tester l'indisponibilité)

-- Données pour tester les consultations sur place multiples
INSERT INTO pret (adherent_id, exemplaire_id, type_pret_id, date_pret, date_retour_prevue) VALUES
(1, 6, 2, '2024-01-23', NULL), -- Marie consultation sur place
(2, 7, 2, '2024-01-23', NULL), -- Pierre autre consultation
(4, 8, 2, '2024-01-24', NULL); -- Jean consultation

INSERT INTO historique_pret (status_pret_id, pret_id, exemplaire_id, date_changement, commentaire) VALUES
-- Consultations terminées immédiatement
(1, 10, 6, '2024-01-23', 'Consultation sur place débutée'),
(2, 10, 6, '2024-01-23', 'Consultation terminée'),
(1, 11, 7, '2024-01-23', 'Consultation sur place débutée'),
(2, 11, 7, '2024-01-23', 'Consultation terminée'),
(1, 12, 8, '2024-01-24', 'Consultation sur place débutée'),
(2, 12, 8, '2024-01-24', 'Consultation terminée');

-- Adhérent sans inscription (pour tester l'accès refusé)
INSERT INTO adherent (type_adherent_id, nom, prenom, adresse, mot_de_passe, dtn) VALUES
(3, 'Sans', 'Inscription', '000 Rue Vide', 'aucun123', '1985-05-15');

-- Pas d'inscription pour cet adhérent (ID 8)

-- Test avec inscription expirée
INSERT INTO adherent (type_adherent_id, nom, prenom, adresse, mot_de_passe, dtn) VALUES
(1, 'Expire', 'Etudiant', '111 Rue Passée', 'expire123', '2000-01-01');

INSERT INTO inscription (adherent_id, date_inscription, date_expiration) VALUES
(9, '2023-01-01', '2023-12-31'); -- Inscription expirée

-- Quelques données supplémentaires pour enrichir les tests
INSERT INTO livre (titre, auteur, edition, isbn, date_sortie, resume, langue, age_min) VALUES
('Guide de Programmation', 'Dev Master', 'Tech Press', '9780000000002', '2023-01-01', 'Guide complet de programmation.', 'Francais', 14),
('Histoire du Monde', 'Historien Pro', 'Savoir Plus', '9780000000003', '2022-01-01', 'Histoire complète du monde.', 'Francais', 12),
('Science pour Tous', 'Scientifique', 'Vulgarisation', '9780000000004', '2023-06-01', 'Science accessible à tous.', 'Francais', 8);

-- Associations aux catégories
INSERT INTO livre_categorie (livre_id, categorie_id) VALUES
(13, 6), -- Guide programmation -> Classique
(14, 6), -- Histoire -> Classique
(15, 2); -- Science -> Science-Fiction

-- Exemplaires pour ces nouveaux livres
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
(13, 1), (13, 2), (13, 1), -- Guide programmation (3 exemplaires)
(14, 1), (14, 2), -- Histoire (2 exemplaires)
(15, 1), (15, 1), (15, 2), (15, 2); -- Science (4 exemplaires)
(14, 1), (14, 2), -- Histoire (2 exemplaires)
(15, 1), (15, 1), (15, 2), (15, 2); -- Science (4 exemplaires)
