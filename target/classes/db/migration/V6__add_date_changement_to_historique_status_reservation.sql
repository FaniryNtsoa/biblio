-- Vérifier si la colonne date_changement existe déjà
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT FROM information_schema.columns 
        WHERE table_name = 'historique_status_reservation' AND column_name = 'date_changement'
    ) THEN
        -- Ajouter la colonne si elle n'existe pas
        ALTER TABLE historique_status_reservation ADD COLUMN date_changement DATE;
        
        -- Mettre à jour les enregistrements existants avec la date_reservation comme valeur par défaut
        UPDATE historique_status_reservation SET date_changement = date_reservation WHERE date_changement IS NULL;
        
        -- Ajouter la contrainte NOT NULL
        ALTER TABLE historique_status_reservation ALTER COLUMN date_changement SET NOT NULL;
    END IF;
END
$$;
