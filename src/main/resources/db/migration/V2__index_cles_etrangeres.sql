-- PostgreSQL n'indexe pas automatiquement les clés étrangères :
-- index sur les colonnes utilisées pour les jointures et filtres courants.
CREATE INDEX idx_utilisateur_entreprise   ON utilisateur (entreprise_id);
CREATE INDEX idx_utilisateur_role         ON utilisateur (role);
CREATE INDEX idx_station_entreprise       ON station (entreprise_id);
CREATE INDEX idx_carburant_station        ON carburant (station_id);
CREATE INDEX idx_agent_station_station    ON agent_station (station_id);
CREATE INDEX idx_vehicule_entreprise      ON vehicule (id_entreprise);
CREATE INDEX idx_vehicule_utilisateur     ON vehicule (id_utilisateur);
CREATE INDEX idx_demande_entreprise       ON demande (entreprise_id);
CREATE INDEX idx_demande_demandeur        ON demande (demandeur_id);
CREATE INDEX idx_demande_statut           ON demande (statut);
CREATE INDEX idx_ticket_entreprise        ON ticket (entreprise_id);
CREATE INDEX idx_ticket_utilisateur       ON ticket (id_utilisateur);
CREATE INDEX idx_ticket_station           ON ticket (id_station);
CREATE INDEX idx_ticket_statut            ON ticket (statut);
CREATE INDEX idx_attribution_chauffeur    ON attribution (chauffeur_id);
CREATE INDEX idx_consommation_entreprise  ON consommation (id_entreprise);
CREATE INDEX idx_consommation_attribution ON consommation (attribution_id);
CREATE INDEX idx_rapport_entreprise       ON rapport (entreprise_id);
