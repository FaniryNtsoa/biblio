-- Ajouter la colonne date_debut_penalite si elle n'existe pas déjà
ALTER TABLE penalite ADD COLUMN IF NOT EXISTS date_debut_penalite DATE;

-- Mettre à jour les enregistrements existants en utilisant date_penalite comme valeur par défaut
UPDATE penalite SET date_debut_penalite = date_penalite WHERE date_debut_penalite IS NULL;

-- Ajouter la contrainte NOT NULL après avoir mis à jour les données
ALTER TABLE penalite ALTER COLUMN date_debut_penalite SET NOT NULL;
