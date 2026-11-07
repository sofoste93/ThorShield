# ThorShield by Sofoste

**Asgard Technology — Le bouclier de notre vaisseau.**

Dossier créé le 5 novembre 2026. Depuis le 7 novembre : premier prototype Java de diagnostic Windows en lecture seule. Python et Rust restent à implémenter.

## Notre intention

Construire un compagnon de sécurité Windows personnel : comprendre l'état du PC, repérer des changements à examiner et proposer des actions compréhensibles. Le premier produit complète les protections Windows existantes. Il ne promet ni détection exhaustive ni protection absolue.

## Le tiroir du projet

- [Plan produit et étapes](docs/PLAN.md)
- [Architecture commune et sécurité](docs/ARCHITECTURE.md)
- [Reprendre le chantier](docs/REPRISE.md)
- [Version Python](versions/python/README.md)
- [Version Java](versions/java/README.md)
- [Version Rust](versions/rust/README.md)

Un dossier unique garde les spécifications et les trois variantes ensemble. Ouvrir `versions/python` dans PyCharm et `versions/java` dans IntelliJ IDEA au moment de leur implémentation ; les projets ne doivent pas obligatoirement résider sous IdeaProjects pour être ouverts dans IDEA.

## Le nom

ThorShield by Sofoste évoque Thor, la protection et une identité personnelle. « Asgard Technology » constitue notre univers de marque. Orthographe confirmée : **ThorShield**, avec le mot anglais *shield* (bouclier). Disponibilité du nom, des domaines et des marques non vérifiée.

## Décision de départ

Choix confirmé le 7 novembre : **Java en premier**, puis Python et Rust sur le même contrat. Voir les commandes dans le [guide Java](versions/java/README.md).

Le prototype lit Defender et les profils de pare-feu sans modifier les paramètres Windows. Les dépendances de compilation sont gérées par Maven. Ce prototype fonctionne localement et ne constitue pas un antivirus.
