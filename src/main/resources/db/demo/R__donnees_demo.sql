-- =====================================================================
-- Données de démonstration (profil dev UNIQUEMENT, jamais en prod)
-- Mot de passe de tous les comptes : Demo@2026
-- Migration répétable et idempotente : rejouée si ce fichier change,
-- les lignes déjà présentes sont ignorées (ON CONFLICT DO NOTHING).
-- =====================================================================

INSERT INTO entreprise (id, nom, adresse) OVERRIDING SYSTEM VALUE VALUES
  (1, 'Transports Sahel SA', 'Avenue Kwame Nkrumah, Ouagadougou')
ON CONFLICT DO NOTHING;
SELECT setval('entreprise_id_seq', GREATEST(1, (SELECT last_value FROM entreprise_id_seq)));

INSERT INTO station (id_station, nom, adresse, ville, statut, entreprise_id) OVERRIDING SYSTEM VALUE VALUES
  (1, 'Station Ouaga 2000', 'Boulevard Mouammar Kadhafi', 'Ouagadougou', 'actif', 1),
  (2, 'Station Bobo Centre', 'Avenue de la Nation',       'Bobo-Dioulasso', 'actif', 1)
ON CONFLICT DO NOTHING;
SELECT setval('station_id_station_seq', GREATEST(2, (SELECT last_value FROM station_id_station_seq)));

-- Comptes (héritage JOINED : ligne dans utilisateur + ligne dans la table du rôle)
INSERT INTO utilisateur (id, type_utilisateur, role, nom, prenom, nom_utilisateur, email, telephone, mot_de_passe, actif, entreprise_id, date_creation) VALUES
  ('00000000-0000-0000-0000-000000000001', 'GESTIONNAIRE',  'GESTIONNAIRE',  'Ouedraogo', 'Awa',     'gestionnaire', 'gestionnaire@demo.fasocarbu.bf', '+22670000001', '$2a$10$OkFEcFNSnX2.rrwbVDRF8OyyxFYsPNP7dGTP8sMvy5.fcn8MvJaY6', true, 1, now()),
  ('00000000-0000-0000-0000-000000000002', 'DEMANDEUR',     'DEMANDEUR',     'Kabore',    'Issa',    'demandeur',    'demandeur@demo.fasocarbu.bf',    '+22670000002', '$2a$10$OkFEcFNSnX2.rrwbVDRF8OyyxFYsPNP7dGTP8sMvy5.fcn8MvJaY6', true, 1, now()),
  ('00000000-0000-0000-0000-000000000003', 'CHAUFFEUR',     'CHAUFFEUR',     'Sawadogo',  'Moussa',  'chauffeur',    'chauffeur@demo.fasocarbu.bf',    '+22670000003', '$2a$10$OkFEcFNSnX2.rrwbVDRF8OyyxFYsPNP7dGTP8sMvy5.fcn8MvJaY6', true, 1, now()),
  ('00000000-0000-0000-0000-000000000004', 'ADMIN_STATION', 'ADMIN_STATION', 'Traore',    'Fatim',   'adminstation', 'admin.station@demo.fasocarbu.bf','+22670000004', '$2a$10$OkFEcFNSnX2.rrwbVDRF8OyyxFYsPNP7dGTP8sMvy5.fcn8MvJaY6', true, 1, now()),
  ('00000000-0000-0000-0000-000000000005', 'AGENT_STATION', 'AGENT_STATION', 'Zongo',     'Paul',    'agentstation', 'agent.station@demo.fasocarbu.bf','+22670000005', '$2a$10$OkFEcFNSnX2.rrwbVDRF8OyyxFYsPNP7dGTP8sMvy5.fcn8MvJaY6', true, 1, now())
ON CONFLICT DO NOTHING;

INSERT INTO gestionnaire  (utilisateur_id)            VALUES ('00000000-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;
INSERT INTO demandeur     (id)                        VALUES ('00000000-0000-0000-0000-000000000002')
ON CONFLICT DO NOTHING;
INSERT INTO chauffeur     (utilisateur_id, permis_conduite) VALUES ('00000000-0000-0000-0000-000000000003', 'BF-PC-2024-0042')
ON CONFLICT DO NOTHING;
INSERT INTO admin_station (id, station_id)            VALUES ('00000000-0000-0000-0000-000000000004', 1)
ON CONFLICT DO NOTHING;
INSERT INTO agent_station (id_utilisateur, station_id) VALUES ('00000000-0000-0000-0000-000000000005', 1)
ON CONFLICT DO NOTHING;

INSERT INTO carburant (id, nom, prix, station_id, admin_station_id) OVERRIDING SYSTEM VALUE VALUES
  (1, 'Super 91', 850, 1, '00000000-0000-0000-0000-000000000004'),
  (2, 'Gasoil',   675, 1, '00000000-0000-0000-0000-000000000004'),
  (3, 'Super 91', 850, 2, NULL),
  (4, 'Gasoil',   675, 2, NULL)
ON CONFLICT DO NOTHING;
SELECT setval('carburant_id_seq', GREATEST(4, (SELECT last_value FROM carburant_id_seq)));

INSERT INTO vehicule (id_vehicule, immatriculation, marque, modele, quota_carburant, carburant_id, id_entreprise, id_utilisateur) OVERRIDING SYSTEM VALUE VALUES
  (1, '11 JK 4521', 'Toyota', 'Hilux',      200, 2, 1, '00000000-0000-0000-0000-000000000003'),
  (2, '11 KL 0937', 'Nissan', 'Navara',     150, 2, 1, NULL),
  (3, '11 MN 7710', 'Toyota', 'Corolla',    100, 1, 1, NULL)
ON CONFLICT DO NOTHING;
SELECT setval('vehicule_id_vehicule_seq', GREATEST(3, (SELECT last_value FROM vehicule_id_vehicule_seq)));

INSERT INTO demande (id, quantite, statut, date_demande, carburant_id, station_id, vehicule_id, entreprise_id, demandeur_id, chauffeur_id) OVERRIDING SYSTEM VALUE VALUES
  (1, 60, 'EN_ATTENTE', now() - interval '1 day', 2, 1, 1, 1, '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003')
ON CONFLICT DO NOTHING;
SELECT setval('demande_id_seq', GREATEST(1, (SELECT last_value FROM demande_id_seq)));
