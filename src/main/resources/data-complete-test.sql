-- Effacer toutes les données existantes
-- ATTENTION: Cette section supprime toutes les données. Utilisez avec précaution.
DELETE FROM historique_prolongement;
DELETE FROM prolongement;
DELETE FROM historique_status_reservation;
DELETE FROM reservation;
DELETE FROM historique_pret;
DELETE FROM penalite;
DELETE FROM pret;
DELETE FROM exemplaire;
DELETE FROM livre_categorie;
DELETE FROM livre;
DELETE FROM inscription;
DELETE FROM adherent;
DELETE FROM gestion_adherent;
DELETE FROM type_adherent;
DELETE FROM categorie;
DELETE FROM etat_exemplaire;
DELETE FROM status_pret;
DELETE FROM status_reservation;
DELETE FROM status_prolongement;
DELETE FROM type_pret;

-- ======================================
-- INSERTION DES DONNÉES DE RÉFÉRENCE
-- ======================================

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
('Aventure'),
('Romance'),
('Horreur'),
('Biographie'),
('Histoire'),
('Poésie');

-- Insertion des types de prêt
INSERT INTO type_pret (nom, sur_place) VALUES 
('a domicile', false), 
('sur place', true);

-- Insertion des types d'adhérents
INSERT INTO type_adherent (nom) VALUES
('Étudiant'),
('Enseignant'),
('Particulier'),
('Senior'),
('Jeune');

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

-- Insertion des statuts de réservation
INSERT INTO status_reservation (nom) VALUES
('EN_ATTENTE'),
('CONFIRMEE'),
('ANNULEE'),
('EXPIREE');

-- Insertion des statuts de prolongement
INSERT INTO status_prolongement (nom) VALUES
('DEMANDE'),
('ACCEPTE'),
('REFUSE');

-- ======================================
-- INSERTION DES LIVRES ET EXEMPLAIRES
-- ======================================

-- Insertion des livres (20 livres de genres variés)
INSERT INTO livre (titre, auteur, edition, isbn, date_sortie, resume, langue, age_min) VALUES
-- Classiques
('Le Seigneur des Anneaux', 'J.R.R. Tolkien', 'Christian Bourgois', '9782266282367', '1954-07-29', 'Une quete epique pour detruire un anneau malefique.', 'Francais', 12),
('1984', 'George Orwell', 'Gallimard', '9782070368226', '1949-06-08', 'Une dystopie sur un regime totalitaire.', 'Francais', 16),
('Harry Potter a l''ecole des sorciers', 'J.K. Rowling', 'Gallimard Jeunesse', '9782070518424', '1997-06-26', 'Un jeune sorcier decouvre son destin.', 'Francais', 8),
('Le Petit Prince', 'Antoine de Saint-Exupery', 'Gallimard', '9782070408504', '1943-04-06', 'Un conte philosophique pour petits et grands.', 'Francais', 6),
('Dune', 'Frank Herbert', 'Robert Laffont', '9782221117535', '1965-08-01', 'Une saga de science-fiction sur une planete desertique.', 'Francais', 14),
('Les Miserables', 'Victor Hugo', 'Pocket', '9782266196830', '1862-01-01', 'L''histoire de Jean Valjean et de sa redemption.', 'Francais', 12),
('Le Crime de l''Orient-Express', 'Agatha Christie', 'Le Masque', '9782702435347', '1934-01-01', 'Hercule Poirot enquete sur un meurtre dans un train.', 'Francais', 10),
('L''Etranger', 'Albert Camus', 'Gallimard', '9782070360022', '1942-05-19', 'Un homme indifferent a son propre destin.', 'Francais', 14),
('Asterix le Gaulois', 'Goscinny et Uderzo', 'Dargaud', '9782012101339', '1961-10-29', 'Les aventures d''un petit gaulois malicieux.', 'Francais', 6),
('Le Vieil Homme et la Mer', 'Ernest Hemingway', 'Gallimard', '9782070368233', '1952-09-01', 'Un vieux pecheur affronte un enorme marlin.', 'Francais', 10),
-- Livres récents
('La Fille du train', 'Paula Hawkins', 'Pocket', '9782266254489', '2015-01-15', 'Un thriller psychologique sur une femme qui observe un couple depuis le train.', 'Francais', 16),
('Hunger Games', 'Suzanne Collins', 'Pocket Jeunesse', '9782266182690', '2008-09-14', 'Dans un futur dystopique, des adolescents s''affrontent dans une arène.', 'Francais', 12),
('Sapiens: Une brève histoire de l''humanité', 'Yuval Noah Harari', 'Albin Michel', '9782226257017', '2015-02-04', 'L''histoire de l''humanité depuis les premiers hommes.', 'Francais', 16),
('L''Arabe du futur', 'Riad Sattouf', 'Allary', '9782370730237', '2014-05-07', 'Une bande dessinée autobiographique sur l''enfance de l''auteur en Syrie.', 'Francais', 14),
('Chanson douce', 'Leïla Slimani', 'Gallimard', '9782070196678', '2016-08-18', 'L''histoire d''une nounou parfaite qui cache un terrible secret.', 'Francais', 16),
-- Livres pour enfants
('Le Gruffalo', 'Julia Donaldson', 'Gallimard Jeunesse', '9782070616237', '1999-03-23', 'Une petite souris rusée fait face à un monstre effrayant.', 'Francais', 3),
('Journal d''un dégonflé', 'Jeff Kinney', 'Seuil', '9782021010268', '2007-04-01', 'Le journal d''un collégien qui raconte sa vie quotidienne.', 'Francais', 8),
('Harry Potter et la Chambre des Secrets', 'J.K. Rowling', 'Gallimard Jeunesse', '9782070524556', '1998-07-02', 'La deuxième année de Harry à Poudlard.', 'Francais', 8),
('Les Royaumes du Nord', 'Philip Pullman', 'Gallimard Jeunesse', '9782070612574', '1995-07-19', 'Une jeune fille part à la recherche de son ami kidnappé.', 'Francais', 10),
('Percy Jackson: Le Voleur de Foudre', 'Rick Riordan', 'Albin Michel', '9782226207906', '2005-06-28', 'Un adolescent découvre qu''il est le fils d''un dieu grec.', 'Francais', 10);

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
(10, 6), (10, 10),
-- La Fille du train (Policier)
(11, 7),
-- Hunger Games (Dystopie, Jeunesse, Aventure)
(12, 3), (12, 4), (12, 10),
-- Sapiens (Histoire)
(13, 14),
-- L'Arabe du futur (Bande Dessinée, Biographie)
(14, 9), (14, 13),
-- Chanson douce (Classique)
(15, 6),
-- Le Gruffalo (Jeunesse, Conte)
(16, 4), (16, 5),
-- Journal d'un dégonflé (Jeunesse, Bande Dessinée)
(17, 4), (17, 9),
-- Harry Potter et la Chambre des Secrets (Fantasy, Jeunesse)
(18, 1), (18, 4),
-- Les Royaumes du Nord (Fantasy, Jeunesse, Aventure)
(19, 1), (19, 4), (19, 10),
-- Percy Jackson (Fantasy, Jeunesse, Aventure)
(20, 1), (20, 4), (20, 10);

-- Insertion des exemplaires pour chaque livre
-- Nous créons plusieurs exemplaires pour chaque livre avec différents états
INSERT INTO exemplaire (livre_id, etat_exemplaire_id) VALUES
-- Le Seigneur des Anneaux (5 exemplaires)
(1, 1), (1, 1), (1, 2), (1, 2), (1, 3),
-- 1984 (4 exemplaires)
(2, 1), (2, 2), (2, 2), (2, 3),
-- Harry Potter à l'école des sorciers (6 exemplaires - populaire)
(3, 1), (3, 1), (3, 1), (3, 2), (3, 2), (3, 3),
-- Le Petit Prince (8 exemplaires - très populaire)
(4, 1), (4, 1), (4, 1), (4, 2), (4, 2), (4, 2), (4, 3), (4, 3),
-- Dune (3 exemplaires)
(5, 1), (5, 2), (5, 3),
-- Les Miserables (3 exemplaires)
(6, 1), (6, 2), (6, 3),
-- Le Crime de l'Orient-Express (4 exemplaires)
(7, 1), (7, 1), (7, 2), (7, 3),
-- L'Etranger (4 exemplaires)
(8, 1), (8, 2), (8, 3), (8, 4),
-- Asterix le Gaulois (5 exemplaires - populaire chez les jeunes)
(9, 1), (9, 1), (9, 2), (9, 2), (9, 3),
-- Le Vieil Homme et la Mer (3 exemplaires)
(10, 1), (10, 2), (10, 3),
-- La Fille du train (3 exemplaires)
(11, 1), (11, 2), (11, 3),
-- Hunger Games (4 exemplaires)
(12, 1), (12, 1), (12, 2), (12, 3),
-- Sapiens (2 exemplaires)
(13, 1), (13, 2),
-- L'Arabe du futur (3 exemplaires)
(14, 1), (14, 2), (14, 3),
-- Chanson douce (2 exemplaires)
(15, 1), (15, 2),
-- Le Gruffalo (4 exemplaires - populaire chez les très jeunes)
(16, 1), (16, 1), (16, 2), (16, 2),
-- Journal d'un dégonflé (3 exemplaires)
(17, 1), (17, 2), (17, 3),
-- Harry Potter et la Chambre des Secrets (5 exemplaires)
(18, 1), (18, 1), (18, 2), (18, 2), (18, 3),
-- Les Royaumes du Nord (3 exemplaires)
(19, 1), (19, 2), (19, 3),
-- Percy Jackson (4 exemplaires)
(20, 1), (20, 1), (20, 2), (20, 3);

-- ======================================
-- INSERTION DES ADHÉRENTS ET INSCRIPTIONS
-- ======================================

-- Insertion de 20 adhérents de test (4 de chaque type)
INSERT INTO adherent (type_adherent_id, nom, prenom, adresse, mot_de_passe, dtn) VALUES
-- Étudiants
(1, 'Dupont', 'Marie', '123 Rue de la Paix, Paris', 'password123', '1995-03-15'),
(1, 'Martin', 'Thomas', '45 Avenue des Champs, Lyon', 'thomas123', '1998-07-22'),
(1, 'Lefevre', 'Julie', '8 Boulevard Saint-Michel, Paris', 'julie456', '1997-11-30'),
(1, 'Dubois', 'Lucas', '67 Rue de la République, Marseille', 'lucas789', '1996-09-12'),
-- Enseignants
(2, 'Moreau', 'Sophie', '12 Avenue Foch, Strasbourg', 'sophie987', '1980-02-18'),
(2, 'Bernard', 'Philippe', '34 Rue Victor Hugo, Bordeaux', 'phil2023', '1975-05-27'),
(2, 'Petit', 'Catherine', '56 Place de la Madeleine, Paris', 'cath1975', '1978-12-03'),
(2, 'Robert', 'Michel', '89 Rue Gambetta, Lille', 'michel456', '1972-08-15'),
-- Particuliers
(3, 'Richard', 'Nathalie', '23 Rue des Lilas, Toulouse', 'nath2023', '1985-06-20'),
(3, 'Simon', 'David', '45 Avenue Jean Jaurès, Nice', 'david123', '1983-04-11'),
(3, 'Laurent', 'Sylvie', '78 Rue Pasteur, Nantes', 'sylvie789', '1990-10-05'),
(3, 'Michel', 'Olivier', '34 Boulevard des Alpes, Grenoble', 'olivier234', '1988-01-23'),
-- Seniors
(4, 'Leroy', 'Jean', '12 Rue de la Liberté, Dijon', 'jean567', '1955-03-30'),
(4, 'Roux', 'Françoise', '56 Avenue de la Mer, Montpellier', 'fran1955', '1952-12-15'),
(4, 'Vincent', 'Gerard', '90 Rue des Fleurs, Reims', 'gerard789', '1948-07-08'),
(4, 'Fournier', 'Monique', '34 Place du Marché, Tours', 'monique123', '1950-09-22'),
-- Jeunes
(5, 'Mercier', 'Alexandre', '45 Rue des Écoles, Angers', 'alex2010', '2010-04-18'),
(5, 'Blanc', 'Emma', '67 Avenue des Pins, Rennes', 'emma2012', '2012-08-07'),
(5, 'Girard', 'Mathis', '23 Boulevard des Oiseaux, Le Mans', 'mathis2011', '2011-11-12'),
(5, 'Bonnet', 'Léa', '56 Rue de la Fontaine, Caen', 'lea2009', '2009-02-28');

-- Insertions d'inscriptions (tous avec des inscriptions actives sauf les derniers de chaque catégorie)
INSERT INTO inscription (adherent_id, date_inscription, date_expiration) VALUES
-- Étudiants
(1, '2023-09-01', '2024-09-01'), -- Marie - active
(2, '2023-10-15', '2024-10-15'), -- Thomas - active
(3, '2023-11-20', '2024-11-20'), -- Julie - active
(4, '2023-01-10', '2023-12-10'), -- Lucas - expirée
-- Enseignants
(5, '2023-09-05', '2024-09-05'), -- Sophie - active
(6, '2023-08-20', '2024-08-20'), -- Philippe - active
(7, '2023-07-15', '2024-07-15'), -- Catherine - active
(8, '2022-12-01', '2023-11-01'), -- Michel - expirée
-- Particuliers
(9, '2023-10-10', '2024-04-10'),  -- Nathalie - active (6 mois)
(10, '2023-11-05', '2024-05-05'), -- David - active (6 mois)
(11, '2023-12-01', '2024-06-01'), -- Sylvie - active (6 mois)
(12, '2023-01-15', '2023-07-15'), -- Olivier - expirée
-- Seniors
(13, '2023-09-10', '2024-09-10'), -- Jean - active
(14, '2023-10-20', '2024-10-20'), -- Françoise - active
(15, '2023-11-15', '2024-11-15'), -- Gerard - active
(16, '2022-12-10', '2023-12-10'), -- Monique - expirée
-- Jeunes
(17, '2023-09-15', '2024-03-15'), -- Alexandre - active (6 mois)
(18, '2023-10-01', '2024-04-01'), -- Emma - active (6 mois)
(19, '2023-11-01', '2024-05-01'), -- Mathis - active (6 mois)
(20, '2023-01-05', '2023-07-05'); -- Léa - expirée

-- ======================================
-- INSERTION DES PRÊTS ET HISTORIQUES
-- ======================================

-- Date actuelle de référence pour les tests (modifiez selon votre besoin)
-- Supposons que nous sommes le 15 mars 2024
-- Préts en cours (différentes dates d'emprunt et de retour prévues)
INSERT INTO pret (adherent_id, exemplaire_id, type_pret_id, date_pret, date_retour_prevue) VALUES
-- Marie (Étudiante) - 3 prêts (limite atteinte)
(1, 1, 1, '2024-03-01', '2024-03-22'), -- Seigneur des Anneaux - en cours
(1, 11, 1, '2024-03-05', '2024-03-26'), -- Harry Potter - en cours
(1, 31, 1, '2024-03-10', '2024-03-31'), -- Crime de l'Orient-Express - en cours
-- Thomas (Étudiant) - 2 prêts
(2, 6, 1, '2024-03-02', '2024-03-23'), -- 1984 - en cours
(2, 35, 1, '2024-03-08', '2024-03-29'), -- L'Étranger - en cours
-- Julie (Étudiante) - 1 prêt en cours, 1 prêt en retard
(3, 16, 1, '2024-03-03', '2024-03-24'), -- Le Petit Prince - en cours
(3, 22, 1, '2024-02-15', '2024-03-08'), -- Dune - en retard
-- Sophie (Enseignante) - 2 prêts
(5, 26, 1, '2024-03-05', '2024-04-04'), -- Les Misérables - en cours
(5, 36, 1, '2024-03-12', '2024-04-11'), -- L'Étranger - en cours
-- Philippe (Enseignant) - 1 prêt + 1 consultation sur place
(6, 41, 1, '2024-03-07', '2024-04-06'), -- Astérix - en cours
(6, 17, 2, '2024-03-14', NULL), -- Le Petit Prince - consultation sur place
-- Nathalie (Particulier) - 2 prêts (limite atteinte)
(9, 46, 1, '2024-03-09', '2024-03-23'), -- Le Vieil Homme et la Mer - en cours
(9, 49, 1, '2024-03-11', '2024-03-25'), -- La Fille du train - en cours
-- Jean (Senior) - 3 prêts
(13, 53, 1, '2024-03-08', '2024-03-29'), -- Hunger Games - en cours
(13, 57, 1, '2024-03-10', '2024-03-31'), -- L'Arabe du futur - en cours
(13, 59, 1, '2024-03-12', '2024-04-02'), -- Chanson douce - en cours
-- Alexandre (Jeune) - 1 prêt en cours, 1 prêt rendu
(17, 61, 1, '2024-03-10', '2024-03-24'), -- Le Gruffalo - en cours
(17, 65, 1, '2024-02-20', '2024-03-05'), -- Journal d'un dégonflé - à rendre

-- Prêts en retard
(11, 70, 1, '2024-02-10', '2024-02-24'), -- Harry Potter 2 - en retard (Sylvie - Particulier)
(15, 74, 1, '2024-02-12', '2024-03-04'), -- Les Royaumes du Nord - en retard (Gerard - Senior)

-- Prêts déjà rendus
(10, 77, 1, '2024-01-15', '2024-01-29'), -- Percy Jackson - rendu à temps (David - Particulier)
(14, 4, 1, '2024-01-20', '2024-02-10'), -- Seigneur des Anneaux - rendu à temps (Françoise - Senior)
(18, 13, 1, '2024-02-01', '2024-02-15'), -- Harry Potter - rendu à temps (Emma - Jeune)
(7, 19, 1, '2024-01-10', '2024-02-09'), -- Le Petit Prince - rendu à temps (Catherine - Enseignante)
(7, 27, 1, '2024-01-25', '2024-02-24'), -- Les Misérables - rendu en retard (Catherine - Enseignante)

-- Consultations sur place (toutes rendues le jour même)
(19, 42, 2, '2024-03-14', NULL), -- Astérix - consultation sur place (Mathis - Jeune)
(10, 18, 2, '2024-03-13', NULL), -- Le Petit Prince - consultation sur place (David - Particulier)
(5, 30, 2, '2024-03-12', NULL); -- Le Crime de l'Orient-Express - consultation sur place (Sophie - Enseignante)

-- Historique des prêts
INSERT INTO historique_pret (status_pret_id, pret_id, exemplaire_id, date_changement, commentaire, date_retour) VALUES
-- Prêts en cours de Marie
(1, 1, 1, '2024-03-01', 'Prêt à domicile - Étudiant', NULL),
(1, 2, 11, '2024-03-05', 'Prêt à domicile - Étudiant', NULL),
(1, 3, 31, '2024-03-10', 'Prêt à domicile - Étudiant', NULL),
-- Prêts en cours de Thomas
(1, 4, 6, '2024-03-02', 'Prêt à domicile - Étudiant', NULL),
(1, 5, 35, '2024-03-08', 'Prêt à domicile - Étudiant', NULL),
-- Prêt en cours et en retard de Julie
(1, 6, 16, '2024-03-03', 'Prêt à domicile - Étudiant', NULL),
(1, 7, 22, '2024-02-15', 'Prêt à domicile - Étudiant', NULL),
(3, 7, 22, '2024-03-09', 'Prêt en retard détecté automatiquement', NULL),
-- Prêts de Sophie
(1, 8, 26, '2024-03-05', 'Prêt à domicile - Enseignant', NULL),
(1, 9, 36, '2024-03-12', 'Prêt à domicile - Enseignant', NULL),
-- Prêts de Philippe
(1, 10, 41, '2024-03-07', 'Prêt à domicile - Enseignant', NULL),
(1, 11, 17, '2024-03-14', 'Consultation sur place débutée', '2024-03-14'),
(2, 11, 17, '2024-03-14', 'Consultation terminée', '2024-03-14'),
-- Prêts de Nathalie
(1, 12, 46, '2024-03-09', 'Prêt à domicile - Particulier', NULL),
(1, 13, 49, '2024-03-11', 'Prêt à domicile - Particulier', NULL),
-- Prêts de Jean
(1, 14, 53, '2024-03-08', 'Prêt à domicile - Senior', NULL),
(1, 15, 57, '2024-03-10', 'Prêt à domicile - Senior', NULL),
(1, 16, 59, '2024-03-12', 'Prêt à domicile - Senior', NULL),
-- Prêts d'Alexandre
(1, 17, 61, '2024-03-10', 'Prêt à domicile - Jeune', NULL),
(1, 18, 65, '2024-02-20', 'Prêt à domicile - Jeune', NULL),
(2, 18, 65, '2024-03-03', 'Livre rendu', '2024-03-03'),
-- Prêts en retard
(1, 19, 70, '2024-02-10', 'Prêt à domicile - Particulier', NULL),
(3, 19, 70, '2024-02-25', 'Prêt en retard détecté automatiquement', NULL),
(1, 20, 74, '2024-02-12', 'Prêt à domicile - Senior', NULL),
(3, 20, 74, '2024-03-05', 'Prêt en retard détecté automatiquement', NULL),
-- Prêts rendus
(1, 21, 77, '2024-01-15', 'Prêt à domicile - Particulier', NULL),
(2, 21, 77, '2024-01-27', 'Livre rendu', '2024-01-27'),
(1, 22, 4, '2024-01-20', 'Prêt à domicile - Senior', NULL),
(2, 22, 4, '2024-02-08', 'Livre rendu', '2024-02-08'),
(1, 23, 13, '2024-02-01', 'Prêt à domicile - Jeune', NULL),
(2, 23, 13, '2024-02-14', 'Livre rendu', '2024-02-14'),
(1, 24, 19, '2024-01-10', 'Prêt à domicile - Enseignant', NULL),
(2, 24, 19, '2024-02-05', 'Livre rendu', '2024-02-05'),
(1, 25, 27, '2024-01-25', 'Prêt à domicile - Enseignant', NULL),
(3, 25, 27, '2024-02-25', 'Prêt en retard détecté automatiquement', NULL),
(2, 25, 27, '2024-03-01', 'Livre rendu en retard', '2024-03-01'),
-- Consultations sur place
(1, 26, 42, '2024-03-14', 'Consultation sur place débutée', '2024-03-14'),
(2, 26, 42, '2024-03-14', 'Consultation terminée', '2024-03-14'),
(1, 27, 18, '2024-03-13', 'Consultation sur place débutée', '2024-03-13'),
(2, 27, 18, '2024-03-13', 'Consultation terminée', '2024-03-13'),
(1, 28, 30, '2024-03-12', 'Consultation sur place débutée', '2024-03-12'),
(2, 28, 30, '2024-03-12', 'Consultation terminée', '2024-03-12');

-- ======================================
-- INSERTION DES PÉNALITÉS
-- ======================================

-- Pénalités pour les prêts en retard
INSERT INTO penalite (pret_id, description, date_penalite, nb_jours_retard, date_debut_penalite, date_fin_penalite, active, duree_jours, date_creation) VALUES
-- Julie - prêt en retard (7 jours de retard)
(7, 'Retard de restitution - Dune', '2024-03-15', 7, '2024-03-15', '2024-03-22', true, 7, '2024-03-15'),
-- Sylvie - prêt en retard (20 jours de retard)
(19, 'Retard de restitution - HP2', '2024-03-15', 20, '2024-03-15', '2024-04-04', true, 20, '2024-03-15'),
-- Gerard - prêt en retard (11 jours de retard)
(20, 'Retard de restitution - Royaumes', '2024-03-15', 11, '2024-03-15', '2024-03-26', true, 11, '2024-03-15'),
-- Catherine - prêt rendu en retard (5 jours de retard)
(25, 'Retard de restitution - Misérables', '2024-03-01', 5, '2024-03-01', '2024-03-06', false, 5, '2024-03-01');

-- ======================================
-- INSERTION DES RÉSERVATIONS
-- ======================================

-- Réservations
INSERT INTO reservation (adherent_id, livre_id, date_reservation, date_expiration) VALUES
-- Réservations en attente
(10, 1, '2024-03-12', '2024-03-19'), -- David réserve Le Seigneur des Anneaux
(14, 3, '2024-03-10', '2024-03-17'), -- Françoise réserve Harry Potter
(18, 5, '2024-03-14', '2024-03-21'), -- Emma réserve Dune
-- Réservations confirmées
(6, 9, '2024-03-08', '2024-03-15'), -- Philippe réserve Astérix
(13, 12, '2024-03-07', '2024-03-14'), -- Jean réserve Hunger Games
-- Réservations annulées
(9, 8, '2024-02-20', '2024-02-27'), -- Nathalie réserve L'Étranger
(2, 10, '2024-02-25', '2024-03-03'), -- Thomas réserve Le Vieil Homme et la Mer
-- Réservations expirées
(5, 18, '2024-02-01', '2024-02-08'), -- Sophie réserve Harry Potter 2
(17, 19, '2024-02-05', '2024-02-12'); -- Alexandre réserve Les Royaumes du Nord

-- Historique des statuts de réservation
INSERT INTO historique_status_reservation (status_reservation_id, reservation_id, date_changement) VALUES
-- Réservations en attente
(1, 1, '2024-03-12'), -- David - en attente
(1, 2, '2024-03-10'), -- Françoise - en attente
(1, 3, '2024-03-14'), -- Emma - en attente
-- Réservations confirmées
(1, 4, '2024-03-08'), -- Philippe - initialement en attente
(2, 4, '2024-03-10'), -- Philippe - confirmée
(1, 5, '2024-03-07'), -- Jean - initialement en attente
(2, 5, '2024-03-09'), -- Jean - confirmée
-- Réservations annulées
(1, 6, '2024-02-20'), -- Nathalie - initialement en attente
(3, 6, '2024-02-23'), -- Nathalie - annulée
(1, 7, '2024-02-25'), -- Thomas - initialement en attente
(3, 7, '2024-02-28'), -- Thomas - annulée
-- Réservations expirées
(1, 8, '2024-02-01'), -- Sophie - initialement en attente
(4, 8, '2024-02-09'), -- Sophie - expirée
(1, 9, '2024-02-05'), -- Alexandre - initialement en attente
(4, 9, '2024-02-13'); -- Alexandre - expirée

-- ======================================
-- INSERTION DES PROLONGEMENTS
-- ======================================

-- Prolongements
INSERT INTO prolongement (pret_id, nb_jour) VALUES
-- Prolongements demandés
(4, 7), -- Thomas demande 7 jours pour 1984
(8, 10), -- Sophie demande 10 jours pour Les Misérables
(14, 14), -- Jean demande 14 jours pour Hunger Games
-- Prolongements acceptés
(1, 7), -- Marie demande 7 jours pour Le Seigneur des Anneaux
(12, 5), -- Nathalie demande 5 jours pour Le Vieil Homme et la Mer
-- Prolongements refusés
(17, 7); -- Alexandre demande 7 jours pour Le Gruffalo

-- Historique des prolongements
INSERT INTO historique_prolongement (status_prolongement_id, prolongement_id, date_changement) VALUES
-- Prolongements demandés
(1, 1, '2024-03-15'), -- Thomas - demandé
(1, 2, '2024-03-14'), -- Sophie - demandé
(1, 3, '2024-03-13'), -- Jean - demandé
-- Prolongements acceptés
(1, 4, '2024-03-10'), -- Marie - initialement demandé
(2, 4, '2024-03-12'), -- Marie - accepté
(1, 5, '2024-03-11'), -- Nathalie - initialement demandé
(2, 5, '2024-03-13'), -- Nathalie - accepté
-- Prolongements refusés
(1, 6, '2024-03-12'), -- Alexandre - initialement demandé
(3, 6, '2024-03-14'); -- Alexandre - refusé
