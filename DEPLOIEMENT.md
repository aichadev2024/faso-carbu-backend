# Déploiement : Neon (base) + Northflank (API)

Architecture cible, entièrement gratuite :

| Partie | Hébergement |
|---|---|
| Base PostgreSQL | Neon (offre Free) |
| API Spring Boot (ce dépôt) | Northflank (offre Sandbox), à partir du `Dockerfile` |
| Site web | Cloudflare Pages (dépôt `faso_carbu_web`) |
| Domaine + HTTPS | Cloudflare |

L'image est réglée pour une petite instance de 512 Mo (voir `JAVA_OPTS` dans
le `Dockerfile`). Testée en profil `prod` avec ces limites : démarrage en
~25 s sur un poste normal (compter 1 à 2 min sur une petite instance),
~300 Mo utilisés.

> **Région :** mettre l'API et la base dans la même zone (Europe de
> préférence), sinon chaque requête traverse l'Atlantique.

---

## 1. Base de données sur Neon

1. Sur <https://console.neon.tech>, créer un projet `fasocarbu`, dans la
   région **AWS Europe (Frankfurt)** si l'API est en Europe (sinon la région
   la plus proche de celle choisie sur Northflank).
2. Garder la base proposée (`neondb`) ou en créer une `fasocarbu`.
3. Bouton **Connect** → désactiver **Connection pooling** pour obtenir l'hôte
   **direct** (sans `-pooler` dans le nom). Flyway a besoin d'une connexion
   directe pour appliquer les migrations.
4. Noter les valeurs de la chaîne affichée :

   ```
   postgresql://UTILISATEUR:MOT_DE_PASSE@ep-xxxx.eu-central-1.aws.neon.tech/neondb?sslmode=require
                └─ DB_USER   └─ DB_PASSWORD └─ DB_HOST                         └─ DB_NAME
   ```

Rien d'autre à faire : au premier démarrage, l'API crée les tables (Flyway).
Aucune donnée de démo n'est chargée en production.

## 2. Secret JWT

Générer une valeur aléatoire (au moins 32 caractères), une seule fois, et la
garder : la changer déconnecte tous les utilisateurs.

```powershell
[Convert]::ToBase64String((1..48 | ForEach-Object { Get-Random -Maximum 256 }))
```

## 3. API sur Northflank

### Projet

1. **Deployment target** : `Northflank Cloud`.
2. **Région** : une région d'Europe si elle est proposée (sinon celle
   recommandée), et créer la base Neon dans la région la plus proche.
3. **Link now** (bandeau bleu) → relier le compte GitHub et autoriser au
   moins le dépôt `faso-carbu-backend`.

### Service

1. **Create new → Service → Combined service** (construit et déploie).
2. **Repository** : `faso-carbu-backend`, branche `master`.
3. **Build type** : `Dockerfile`, chemin `/Dockerfile`, contexte `/`.
4. **Resources** : la plus petite offre comprise dans le Sandbox
   (512 Mo de mémoire ; ne pas descendre en dessous).
5. **Networking → Ports** : port `9090`, protocole `HTTP`, cocher
   **Publicly expose this port**. Northflank donne une adresse
   `https://…code.run`.
6. **Health checks** : en ajouter deux, de type HTTP, chemin
   `/actuator/health`, port `9090` :
   - *Readiness* : délai initial **120 s** ;
   - *Liveness* : délai initial **180 s**.

   La petite instance met du temps à démarrer Java : un délai trop court
   fait redémarrer le service en boucle.
7. **Environment → Runtime variables** :

   | Variable | Valeur |
   |---|---|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `DB_HOST` | hôte Neon direct (`ep-xxxx.eu-central-1.aws.neon.tech`) |
   | `DB_NAME` | `neondb` (ou le nom choisi) |
   | `DB_USER` | utilisateur Neon |
   | `DB_PASSWORD` 🔒 | mot de passe Neon |
   | `DB_SSLMODE` | `require` |
   | `JWT_SECRET` 🔒 | valeur générée à l'étape 2 |
   | `CORS_ALLOWED_ORIGINS` | adresse du site web, ex. `https://app.ton-domaine.com` (plusieurs : séparées par des virgules, sans espace) |
   | `BREVO_API_KEY` 🔒 | **nouvelle** clé Brevo (l'ancienne est dans l'historique public du dépôt) |
   | `FIREBASE_CONFIG_JSON` 🔒 | contenu du JSON du compte de service Firebase, sur une ligne (facultatif : sans lui, pas de notifications push) |

   🔒 : valeurs secrètes. Les mettre dans un **Secret group** du projet
   (Secrets → Add secret group, rattaché au service) plutôt qu'en clair.

   Facultatives : `DB_POOL_SIZE` (défaut 3), `JAVA_OPTS` (réglages JVM),
   `SWAGGER_ENABLED=true` pour ouvrir temporairement la doc de l'API.

8. **Create service**. Dans les logs du conteneur, attendre :
   `Successfully applied 2 migrations` puis `Started FasocarbuApplication`.
9. Vérifier : `https://<adresse>.code.run/actuator/health` doit répondre
   `{"status":"UP",...}`.

Chaque fusion dans `master` reconstruit et redéploie automatiquement l'API.

## 4. Domaine via Cloudflare (facultatif mais conseillé)

Pour que l'appli mobile utilise une adresse stable (`https://api.ton-domaine.com`)
même si l'hébergeur change :

1. Northflank → service → **Networking → Domains** (ou Team → Domains) :
   ajouter `api.ton-domaine.com`. Northflank indique un enregistrement de
   vérification et la cible CNAME.
2. Cloudflare → DNS : créer les enregistrements demandés (`CNAME api` → cible
   Northflank). Laisser le proxy **désactivé (nuage gris)** le temps que
   Northflank valide le domaine et émette son certificat.
3. Compiler le web et le mobile avec
   `--dart-define=API_URL=https://api.ton-domaine.com/api`.

## 5. Premier compte

La base de production est vide. Créer le premier gestionnaire depuis la page
d'inscription du site web (ou `POST /api/auth/register`) ; l'inscription
publique ne crée que des gestionnaires. Les autres comptes (demandeurs,
chauffeurs, stations) se créent ensuite depuis l'application.

## Limites connues de l'offre gratuite

- **Photos de profil** : le disque du conteneur n'est pas permanent ; les
  photos envoyées sont perdues à chaque redéploiement. À déplacer vers un
  stockage objet (ex. Cloudflare R2) si la fonction est utilisée.
- **Ressources** : petite instance. Suffisant pour un usage modéré.
- **Neon** met la base en veille après 5 min sans activité : la première
  requête suivante prend 1 à 2 s de plus.
- Les offres gratuites changent souvent : vérifier de temps en temps les
  conditions de Northflank et de Neon.

## En cas de problème

| Symptôme | Cause probable |
|---|---|
| Le service redémarre en boucle, health check KO | délai initial trop court (120–180 s) ou mauvais port (doit être `9090`) |
| `FATAL: password authentication failed` | `DB_USER` / `DB_PASSWORD` |
| `Connection attempt failed` / timeout | `DB_HOST` erroné ou hôte `-pooler` |
| `Could not resolve placeholder 'JWT_SECRET'` | variable manquante (ou secret group non rattaché au service) |
| Erreur CORS dans le navigateur | `CORS_ALLOWED_ORIGINS` ne contient pas l'adresse exacte du site (schéma `https://` compris, sans `/` final) |
| `OutOfMemoryError` / conteneur tué (OOMKilled) | instance trop petite, ou réduire `MaxRAMPercentage` dans `JAVA_OPTS` |
| Le build échoue sur Northflank | vérifier *Build type = Dockerfile*, chemin `/Dockerfile`, contexte `/` |
