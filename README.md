# 🛒 SmartShop - Système de Gestion Commerciale B2B

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Liquibase](https://img.shields.io/badge/Liquibase-4.20-red)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-purple)

## 📋 Description

SmartShop est une application web de gestion commerciale destinée à **MicroTech Maroc**, distributeur B2B de matériel informatique basé à Casablanca. 

L'application permet de gérer :
- ✅ 650 clients actifs
- ✅ Système de fidélité à remises progressives
- ✅ Paiements fractionnés multi-moyens par facture
- ✅ Traçabilité complète des événements financiers
- ✅ Gestion optimisée de la trésorerie

## 🏗️ Architecture

### **Backend REST API**
- API REST uniquement (pas de frontend)
- Tests via Postman / Swagger
- Authentification HTTP Session (pas de JWT)
- Architecture en couches (Controller → Service → Repository)

### **Stack Technique**
- **Framework** : Spring Boot 2.7.18
- **Langage** : Java 8+
- **Base de données** : PostgreSQL / MySQL
- **Migrations** : Liquibase
- **ORM** : Spring Data JPA / Hibernate
- **Mapping** : MapStruct
- **Validation** : Bean Validation
- **Documentation** : SpringDoc OpenAPI (Swagger)
- **Tests** : JUnit 5, Mockito

## 📂 Structure du Projet

```
smartshop/
├── src/main/java/com/microtech/smartshop/
│   ├── config/          # Configurations
│   ├── entity/          # Entités JPA
│   ├── enums/           # Enumerations
│   ├── repository/      # Repositories Spring Data
│   ├── service/         # Services métier
│   ├── dto/             # Data Transfer Objects
│   ├── mapper/          # Mappers MapStruct
│   ├── controller/      # Controllers REST
│   ├── util/            # Utilitaires
│   ├── exception/       # Gestion des erreurs
│   └── interceptor/     # Interceptors HTTP
│
├── src/main/resources/
│   ├── application.yml              # Configuration principale
│   ├── application-dev.yml          # Profil développement
│   ├── application-prod.yml         # Profil production
│   └── db/changelog/                # Migrations Liquibase
│       ├── db.changelog-master.yaml
│       └── changes/
│
└── src/test/                        # Tests unitaires et intégration
```

## 🚀 Installation

### **Prérequis**
- Java 8+ (JDK)
- Maven 3.8+
- PostgreSQL 15+ ou MySQL 8+
- Git

### **Cloner le projet**
```bash
git clone https://github.com/microtech/smartshop.git
cd smartshop
```

### **Configurer la base de données**
```bash
# PostgreSQL
createdb smartshop_db

# Modifier application-dev.yml avec vos identifiants
```

### **Lancer l'application**
```bash
# Mode développement
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Ou compiler puis exécuter
mvn clean install
java -jar target/smartshop-1.0.0.jar --spring.profiles.active=dev
```

### **Accéder à Swagger**
```
http://localhost:8080/swagger-ui.html
```

## 📊 Fonctionnalités Principales

### **1. Gestion des Clients**
- CRUD complet des clients
- Suivi automatique des statistiques (commandes, montant cumulé)
- Historique des commandes par client

### **2. Système de Fidélité Automatique**
- **BASIC** : Client par défaut
- **SILVER** : 3 commandes OU 1,000 DH cumulés
- **GOLD** : 10 commandes OU 5,000 DH cumulés
- **PLATINUM** : 20 commandes OU 15,000 DH cumulés

Remises automatiques selon niveau :
- SILVER : 5% (si commande ≥ 500 DH)
- GOLD : 10% (si commande ≥ 800 DH)
- PLATINUM : 15% (si commande ≥ 1,200 DH)

### **3. Gestion des Produits**
- CRUD avec soft delete
- Gestion du stock en temps réel
- Filtrage et pagination

### **4. Gestion des Commandes**
- Commandes multi-produits
- Calcul automatique : sous-total HT, remises, TVA 20%, total TTC
- Statuts : PENDING, CONFIRMED, CANCELED, REJECTED
- Application codes promo (format PROMO-XXXX)

### **5. Paiements Multi-Moyens**
- Types : ESPÈCES, CHÈQUE, VIREMENT
- Limite légale espèces : 20,000 DH
- Paiements fractionnés par commande
- Traçabilité complète (dates, références, statuts)

### **6. Règles Métier**
- Validation du stock avant commande
- Commande confirmable uniquement si totalement payée
- Mise à jour automatique niveau fidélité après confirmation
- Gestion centralisée des exceptions

## 🧪 Tests

```bash
# Tests unitaires
mvn test

# Tests avec couverture
mvn test jacoco:report

# Tests d'intégration
mvn verify
```

## 📦 Build

```bash
# Build sans tests
mvn clean package -DskipTests

# Build complet
mvn clean install
```

## 🔧 Configuration

### **Profils disponibles**
- `dev` : Développement local
- `prod` : Production
- `test` : Tests automatisés

### **Variables d'environnement**
```yaml
# Taux TVA (configurable)
app.tax.vat-rate: 0.20

# Limite paiement espèces
app.payment.cash-limit: 20000.00

# Pagination
app.pagination.default-page-size: 10
app.pagination.max-page-size: 100
```

## 🗃️ Base de Données

### **Migrations Liquibase**
Les migrations sont automatiques au démarrage :
```bash
# Voir le statut
mvn liquibase:status

# Rollback
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

### **Tables principales**
- `users` : Utilisateurs (ADMIN/CLIENT)
- `clients` : Clients B2B
- `products` : Produits
- `orders` : Commandes
- `order_items` : Lignes de commande
- `payments` : Paiements
- `promo_codes` : Codes promotionnels
- `tier_history` : Historique niveaux fidélité

## 👥 Rôles & Permissions

### **ADMIN** (Employé MicroTech)
- Gestion complète (CRUD) : clients, produits, commandes
- Enregistrement des paiements
- Validation/annulation des commandes
- Consultation de toutes les données

### **CLIENT** (Entreprise cliente)
- Consultation de son profil et statistiques
- Historique de ses propres commandes
- Consultation du catalogue produits (lecture seule)

## 📖 Documentation API

### **Swagger UI**
```
http://localhost:8080/swagger-ui.html
```

### **OpenAPI JSON**
```
http://localhost:8080/api-docs
```

### **Postman Collection**
Disponible dans `/docs/postman/SmartShop.postman_collection.json`

## 🐛 Gestion des Erreurs

Tous les endpoints retournent des erreurs JSON structurées :
```json
{
  "timestamp": "2025-11-25T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Client avec l'ID 999 n'existe pas",
  "path": "/api/clients/999"
}
```

### **Codes HTTP**
- `200` : Succès
- `201` : Créé
- `400` : Erreur de validation
- `401` : Non authentifié
- `403` : Accès refusé
- `404` : Ressource inexistante
- `422` : Règle métier violée
- `500` : Erreur serveur

## 🤝 Contribution

### **Convention de commit**
```
feat: Nouvelle fonctionnalité
fix: Correction de bug
docs: Documentation
refactor: Refactoring
test: Tests
chore: Maintenance
```

### **Branches**
- `main` : Production
- `develop` : Développement
- `feature/xxx` : Nouvelles fonctionnalités
- `hotfix/xxx` : Corrections urgentes

## 📜 Licence

Propriété de **MicroTech Maroc** - Tous droits réservés

## 👨‍💻 Auteurs

- **Équipe MicroTech** - Développement initial

## 📞 Contact

- **Email** : contact@microtech.ma
- **Site web** : https://www.microtech.ma
- **Support** : support@microtech.ma

---

**Version** : 1.0.0  
**Dernière mise à jour** : 25 Novembre 2025  
**Statut** : 🚧 En développement actif
