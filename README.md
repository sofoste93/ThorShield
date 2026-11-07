# ThorShield by Sofoste

[English](#english) | [Français](#français)

## English

**Asgard Technology — Our ship's shield.**

Project created on November 5, 2026. Since November 7: an initial Java prototype for read-only Windows diagnostics. Python and Rust have not been implemented yet.

### Purpose

Build a personal Windows security companion: understand the PC's status, identify changes worth reviewing, and suggest understandable actions. The first product complements existing Windows protections. It promises neither exhaustive detection nor absolute protection.

### Project directory

- [Product plan and milestones (French)](docs/PLAN.md)
- [Shared architecture and security (French)](docs/ARCHITECTURE.md)
- [Resume development (French)](docs/REPRISE.md)
- [Python version](versions/python/README.md)
- [Java version](versions/java/README.md)
- [Rust version](versions/rust/README.md)

A single directory keeps the specifications and all three variants together. Open `versions/python` in PyCharm and `versions/java` in IntelliJ IDEA when working on them; an IDEA project does not need to live under IdeaProjects.

### Name

ThorShield by Sofoste evokes Thor, protection, and a personal identity. “Asgard Technology” is our brand universe. Confirmed spelling: **ThorShield**, using the English word *shield*. Name, domain, and trademark availability have not been checked.

### Starting decision

Confirmed on November 7: **Java first**, followed by Python and Rust using the same contract. See the commands in the [Java guide](versions/java/README.md).

The prototype reads Defender and firewall profiles without changing Windows settings. Maven manages build dependencies. The prototype runs locally and is not an antivirus.

### Documentation languages

All project README files are maintained in **English and French**, with equivalent instructions, features, and limitations. Other documents currently remain in French unless indicated otherwise.

### Release previews

See the [v0.1.0 release notes and three synthetic CLI previews](docs/releases/v0.1.0.md). The images show demonstration data, not the security state of a real PC or a graphical application.

## Français

**Asgard Technology — Le bouclier de notre vaisseau.**

Dossier créé le 5 novembre 2026. Depuis le 7 novembre : premier prototype Java de diagnostic Windows en lecture seule. Python et Rust restent à implémenter.

### Notre intention

Construire un compagnon de sécurité Windows personnel : comprendre l'état du PC, repérer des changements à examiner et proposer des actions compréhensibles. Le premier produit complète les protections Windows existantes. Il ne promet ni détection exhaustive ni protection absolue.

### Le tiroir du projet

- [Plan produit et étapes](docs/PLAN.md)
- [Architecture commune et sécurité](docs/ARCHITECTURE.md)
- [Reprendre le chantier](docs/REPRISE.md)
- [Version Python](versions/python/README.md)
- [Version Java](versions/java/README.md)
- [Version Rust](versions/rust/README.md)

Un dossier unique garde les spécifications et les trois variantes ensemble. Ouvrir `versions/python` dans PyCharm et `versions/java` dans IntelliJ IDEA au moment de leur implémentation ; les projets ne doivent pas obligatoirement résider sous IdeaProjects pour être ouverts dans IDEA.

### Le nom

ThorShield by Sofoste évoque Thor, la protection et une identité personnelle. « Asgard Technology » constitue notre univers de marque. Orthographe confirmée : **ThorShield**, avec le mot anglais *shield* (bouclier). Disponibilité du nom, des domaines et des marques non vérifiée.

### Décision de départ

Choix confirmé le 7 novembre : **Java en premier**, puis Python et Rust sur le même contrat. Voir les commandes dans le [guide Java](versions/java/README.md).

Le prototype lit Defender et les profils de pare-feu sans modifier les paramètres Windows. Les dépendances de compilation sont gérées par Maven. Ce prototype fonctionne localement et ne constitue pas un antivirus.

### Langues de documentation

Tous les README du projet sont maintenus en **anglais et en français**, avec des instructions, fonctionnalités et limites équivalentes. Les autres documents restent actuellement en français, sauf indication contraire.

### Aperçus de la release

Voir les [notes v0.1.0 et les trois aperçus CLI fictifs](docs/releases/v0.1.0.md). Les images montrent des données de démonstration, pas l'état de sécurité d'un vrai PC ni une interface graphique.
