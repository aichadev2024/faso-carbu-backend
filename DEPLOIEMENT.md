# Déploiement : Neon (base) + Koyeb (API)

Architecture cible, entièrement gratuite :

| Partie | Hébergement |
|---|---|
| Base PostgreSQL | Neon |
| API Spring Boot (ce dépôt) | Koyeb, à partir du `Dockerfile` |
| Site web | Cloudflare Pages (dépôt `faso_carbu_web`) |
| Domaine + HTTPS | Cloudflare |

L'image est réglée pour une instance de 512 Mo / 0,1 vCPU (voir `JAVA_OPTS`
dans le `Dockerfile`). Testée en profil `prod` avec ces limites : démarrage
en ~25 s sur un poste normal (compter 1 à 2 min sur Koyeb), ~300 Mo utilisés.

---

## 1. Base de données sur Neon

1. Sur <https://console.neon.tech>, créer un projet `fasocarbu`,
   **région AWS Europe (Frankfurt)** (même région que l'API).
2. Garder la base proposée (`neondb`) ou en créer une `fasocarbu`.
3. Bouton **Connect** → décocher **Connection pooling** pour obtenir l'hôte
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

## 3. API sur Koyeb

1. Sur <https://app.koyeb.com> : **Create Service → Web service → GitHub**,
   choisir le dépôt `faso-carbu-backend`, branche `master`.
2. **Builder** : `Dockerfile` (détecté à la racine).
3. **Instance** : `Free`, **région** : Frankfurt.
4. **Port** : `9090`, protocole HTTP, route `/`.
5. **Health check** : HTTP, chemin `/actuator/health`, port `9090`.
   Mettre un **délai initial (grace period) d'au moins 120 s** : la petite
   instance met du temps à démarrer Java.
6. **Variables d'environnement** — cocher *Secret* pour celles marquées 🔒 :

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

   Facultatives : `DB_POOL_SIZE` (défaut 3), `JAVA_OPTS` (réglages JVM),
   `SWAGGER_ENABLED=true` pour ouvrir temporairement la doc de l'API.

7. **Deploy**. Dans les logs, attendre :
   `Successfully applied 2 migrations` puis `Started FasocarbuApplication`.
8. Vérifier : `https://<service>.koyeb.app/actuator/health` doit répondre
   `{"status":"UP",...}`.

Chaque fusion dans `master` redéploie automatiquement l'API.

## 4. Domaine via Cloudflare (facultatif mais conseillé)

Pour que l'appli mobile utilise une adresse stable (`https://api.ton-domaine.com`)
même si l'hébergeur change :

1. Koyeb → service → **Domains → Add domain** : `api.ton-domaine.com`.
   Koyeb indique la cible CNAME à utiliser.
2. Cloudflare → DNS → **Add record** : type `CNAME`, nom `api`, cible donnée
   par Koyeb. Laisser le proxy **désactivé (nuage gris)** le temps que Koyeb
   valide le domaine et émette son certificat.
3. Compiler le web et le mobile avec
   `--dart-define=API_URL=https://api.ton-domaine.com/api`.

## 5. Premier compte

La base de production est vide. Créer le premier gestionnaire depuis la page
d'inscription du site web (ou `POST /api/auth/register`), puis les autres
comptes depuis l'application.

## Limites connues de l'offre gratuite

- **Photos de profil** : le disque de l'instance Koyeb n'est pas permanent ;
  les photos envoyées sont perdues à chaque redéploiement. À déplacer vers un
  stockage objet (ex. Cloudflare R2) si la fonction est utilisée.
- **Ressources** : 0,1 vCPU. Suffisant pour un usage modéré ; pour plus de
  charge, passer à une instance payante ou augmenter `DB_POOL_SIZE`.
- **Neon** met la base en veille après 5 min sans activité : la première
  requête suivante prend 1 à 2 s de plus.

## En cas de problème

| Symptôme | Cause probable |
|---|---|
| Le service redémarre en boucle, health check KO | délai initial trop court (mettre 120–180 s) |
| `FATAL: password authentication failed` | `DB_USER` / `DB_PASSWORD` |
| `Connection attempt failed` / timeout | `DB_HOST` erroné ou hôte `-pooler` |
| `Could not resolve placeholder 'JWT_SECRET'` | variable manquante |
| Erreur CORS dans le navigateur | `CORS_ALLOWED_ORIGINS` ne contient pas l'adresse exacte du site (schéma `https://` compris, sans `/` final) |
| `OutOfMemoryError` dans les logs | réduire `MaxRAMPercentage` dans `JAVA_OPTS`, ou instance plus grande |
