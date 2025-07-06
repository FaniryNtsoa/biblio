-- Ajouter la colonne date_creation si elle n'existe pas
ALTER TABLE penalite ADD COLUMN IF NOT EXISTS date_creation DATE;

-- Mettre à jour les enregistrements existants avec la date du jour
UPDATE penalite SET date_creation = CURRENT_DATE WHERE date_creation IS NULL;

-- Ajouter la contrainte NOT NULL après avoir mis à jour les données
ALTER TABLE penalite ALTER COLUMN date_creation SET NOT NULL;
