# FasoCarbu — API backend

API REST de la plateforme FasoCarbu : gestion des demandes de carburant, des
tickets à QR code, des stations et des consommations des entreprises.

**Stack** : Java 21 · Spring Boot 3.5 · Spring Security (JWT) · JPA/Hibernate ·
PostgreSQL 16 · Flyway · springdoc OpenAPI · Firebase Cloud Messaging.

Applications clientes : [`faso_carbu_web`](https://github.com/aichadev2024/faso_carbu_web)
(dashboard gestionnaire) et [`faso_carbu_mobile`](https://github.com/aichadev2024/faso_carbu_mobile)
(agents de station et chauffeurs).

## Démarrage rapide

Prérequis : JDK 21 et PostgreSQL 16 (en local ou via Docker).

```bash
cp .env.example .env          # puis remplir les valeurs
docker compose up -d postgres # ou un PostgreSQL local avec la base fasocarbu_db
./mvnw spring-boot:run
```

- API : http://localhost:9090/api
- Swagger UI : http://localhost:9090/swagger-ui/index.html

Pour tout lancer dans Docker (base + API) : `docker compose up -d --build`.

## Profils

| Profil | Usage | Données |
|--------|-------|---------|
| `dev` (défaut) | Développement local | Schéma + données de démo |
| `prod` | Déploiement | Schéma uniquement. Swagger désactivé, SSL Postgres requis |

Activer un profil : `SPRING_PROFILES_ACTIVE=prod`.

### Comptes de démo (profil dev)

Mot de passe commun : `Demo@2026`

| Rôle | Email |
|------|-------|
| Gestionnaire | gestionnaire@demo.fasocarbu.bf |
| Demandeur | demandeur@demo.fasocarbu.bf |
| Chauffeur | chauffeur@demo.fasocarbu.bf |
| Admin station | admin.station@demo.fasocarbu.bf |
| Agent station | agent.station@demo.fasocarbu.bf |

## Base de données

Le schéma est versionné avec **Flyway** (`src/main/resources/db/`) :

- `migration/V1__schema_initial.sql` : schéma initial
- `migration/V2__index_cles_etrangeres.sql` : index de performance
- `demo/R__donnees_demo.sql` : données de démo, chargées **uniquement** en profil `dev`

Hibernate est en `ddl-auto=validate` : il vérifie que les entités correspondent
au schéma, mais ne modifie jamais la base. Pour faire évoluer le schéma :

1. Modifier l'entité JPA.
2. Ajouter une migration `V3__description_courte.sql` (ne jamais modifier une migration déjà appliquée).
3. Redémarrer : Flyway l'applique et Hibernate valide.

## Configuration

Toute la configuration sensible passe par des variables d'environnement.
La liste complète est dans [`.env.example`](.env.example). Le fichier `.env` est
lu automatiquement au démarrage et n'est jamais commité.

| Variable | Description |
|----------|-------------|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | Connexion PostgreSQL |
| `JWT_SECRET` | Clé de signature des tokens (≥ 32 caractères, **obligatoire en prod**) |
| `CORS_ALLOWED_ORIGINS` | Origines autorisées, séparées par des virgules |
| `BREVO_API_KEY` | Envoi des emails (réinitialisation de mot de passe) |
| `FIREBASE_CONFIG_JSON` | JSON du compte de service Firebase, ou chemin vers ce fichier |
| `UPLOAD_DIR` | Dossier des photos de profil |

## Structure du code

```
src/main/java/com/fasocarbu/fasocarbu/
├── core/                 # transverse
│   ├── config/           # sécurité, CORS, OpenAPI
│   ├── security/         # JWT, UserDetails
│   ├── exception/        # gestion globale des erreurs
│   ├── firebase/         # initialisation FCM
│   └── util/             # QR codes
└── modules/              # un dossier par domaine métier
    ├── auth/  user/  entreprise/  station/
    ├── demande/  ticket/  consommation/  vehicule/
    └── notification/
        ├── domain/       # entités JPA
        ├── dto/
        ├── repository/
        ├── service/
        └── web/          # contrôleurs REST
```
