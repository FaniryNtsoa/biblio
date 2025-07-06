-- Mettre à jour la table pour ajouter la colonne manquante si elle n'existe pas
ALTER TABLE penalite ADD COLUMN IF NOT EXISTS duree_jours INT;

-- Mettre à jour tous les enregistrements existants pour définir duree_jours égal à nb_jours_retard
UPDATE penalite SET duree_jours = nb_jours_retard WHERE duree_jours IS NULL;

-- Ajouter la contrainte NOT NULL après avoir mis à jour les données
ALTER TABLE penalite ALTER COLUMN duree_jours SET NOT NULL;
