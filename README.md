# ParcelFlow — exercice Design Patterns en Java 21

## Contexte professionnel

ParcelFlow est une petite plateforme utilisée par un centre logistique pour préparer et suivre des expéditions de colis.

L'application actuelle fonctionne, mais toute la logique est regroupée dans `LegacyShippingService`. Chaque nouvelle catégorie d'expédition, règle tarifaire, option ou moyen de notification oblige à modifier cette classe.

Votre mission consiste à faire évoluer l'application en conservant le scénario existant et en mettant en place uniquement les cinq Design Patterns demandés.

## Exécution du projet de départ

```bash
mvn clean compile exec:java
```

Le scénario crée deux expéditions et fait évoluer leur statut. Observez notamment les numéros de suivi produits par les deux instances du service.

## Travail demandé

### 1. Garantir l'unicité des numéros de suivi — Singleton

Créer un `TrackingNumberGenerator` responsable de la génération des numéros au format :

```text
PF-2026-000001
PF-2026-000002
```

Une seule instance doit être utilisée dans tout le processus Java, y compris lorsque plusieurs services d'expédition existent. L'implémentation doit être sûre dans un contexte multithread.

**Design Pattern imposé : Singleton.**

**Raison :** le projet de départ conserve un compteur dans chaque instance de `LegacyShippingService`. Deux services peuvent donc générer le même numéro. Une source unique garantit la cohérence des références.

### 2. Construire le bon type d'expédition — Factory

Prendre en charge trois catégories :

| Type | Délai indicatif | Particularité |
|---|---:|---|
| `STANDARD` | 3 à 5 jours | Transport classique |
| `EXPRESS` | 24 heures | Traitement prioritaire |
| `REFRIGERATED` | 24 à 48 heures | Maintien de la chaîne du froid |

Créer un produit commun et une classe concrète par catégorie. Déplacer la sélection et la construction de ces objets dans une fabrique.

**Design Pattern imposé : Factory.**

**Raison :** le service contient actuellement un `switch` de construction. La fabrique isole cette décision et permet d'ajouter un nouveau type sans disperser sa création dans l'application.

### 3. Faire varier le calcul tarifaire — Strategy

Extraire le calcul du tarif de base dans des stratégies interchangeables.

Les règles actuelles doivent être conservées :

| Type | Forfait de départ |
|---|---:|
| `STANDARD` | 8,00 € |
| `EXPRESS` | 18,00 € |
| `REFRIGERATED` | 28,00 € |

Le poids et la zone de destination continuent à influencer le prix. Une stratégie doit pouvoir être sélectionnée sans placer le calcul complet dans le service principal.

**Design Pattern imposé : Strategy.**

**Raison :** les algorithmes tarifaires sont susceptibles d'évoluer indépendamment. Strategy évite un grand `switch` et permet d'introduire une tarification promotionnelle ou internationale sans modifier les autres calculs.

### 4. Réagir aux changements de statut — Observer

Une expédition peut passer par les statuts suivants :

```text
CREATED → PREPARED → IN_TRANSIT → DELIVERED
```

Lors d'un changement de statut, publier un événement. Mettre en place au minimum deux observateurs :

| Observateur | Réaction attendue |
|---|---|
| Notification client | Informe le destinataire du nouveau statut |
| Audit logistique | Conserve une trace datée du changement |

La classe représentant l'expédition ne doit connaître aucune implémentation concrète de notification ou d'audit.

**Design Pattern imposé : Observer.**

**Raison :** le code actuel appelle directement les deux traitements. Observer permet d'ajouter ultérieurement un tableau de suivi, des statistiques ou une alerte de retard sans modifier le cœur métier.

### 5. Ajouter des options cumulables — Decorator

Une offre d'expédition possède un prix et une description. Elle peut recevoir zéro, une ou plusieurs options :

| Option | Supplément |
|---|---:|
| Assurance | 5,00 € |
| Signature à la réception | 2,50 € |
| Traitement fragile | 4,00 € |

Créer une abstraction commune pour l'offre de transport, puis un décorateur par option. Les options doivent être cumulables dans n'importe quel ordre.

Exemple attendu :

```text
Expédition EXPRESS + assurance + signature
Prix = tarif EXPRESS calculé + 5,00 € + 2,50 €
```

**Design Pattern imposé : Decorator.**

**Raison :** l'utilisation de booléens et de conditions devient rapidement difficile à maintenir lorsque les options se multiplient. Decorator permet de composer les services sans créer une classe pour chaque combinaison.

## Contraintes générales

- utiliser Java 21 ;
- ne pas ajouter de framework ;
- conserver un point d'entrée exécutable ;
- supprimer progressivement la logique devenue inutile dans `LegacyShippingService` ;
- utiliser les interfaces pour représenter les rôles variables ;
- ne pas utiliser un Design Pattern en dehors des cinq cas demandés ;
- conserver un code compilable après chaque étape.

## Résultat fonctionnel attendu

Le programme final doit créer plusieurs expéditions sans doublon de numéro de suivi, sélectionner leur construction et leur tarif selon leur type, cumuler librement les options, puis prévenir tous les observateurs lors des changements de statut.

