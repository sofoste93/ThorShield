# ThorShield — Python

[English](#english) | [Français](#français)

## English

Status: directory prepared; implementation follows the Java MVP, as decided on November 7, 2026.

Role: second implementation of the read-only Windows diagnostic tool. Prefer the standard library and add external dependencies only when needed. Use a project-specific virtual environment. Select a maintained Python version when implementation starts.

Planned structure: `src/thorshield/{collectors,models,rules,reporting}`, a CLI command, and `tests`. A graphical interface may follow the MVP.

Complete when Defender and firewall checks produce the shared contract, handle access denied, and match Windows observations. See [the plan (French)](../../docs/PLAN.md).

## Français

Statut : emplacement préparé ; implémentation après le MVP Java, suivant le choix du 7 novembre 2026.

Rôle : seconde implémentation du diagnostic Windows en lecture seule. Bibliothèque standard privilégiée et dépendances externes ajoutées seulement si nécessaires. Environnement virtuel propre au projet. Version Python maintenue à sélectionner au démarrage.

Structure prévue : `src/thorshield/{collectors,models,rules,reporting}`, commande CLI et `tests`. Interface graphique éventuelle après le MVP.

Terminé quand les contrôles Defender et pare-feu produisent le contrat commun, gèrent les accès refusés et correspondent aux observations Windows. Voir [le plan](../../docs/PLAN.md).
