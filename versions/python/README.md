# ThorShield — Python

Statut : emplacement préparé ; implémentation après le MVP Java, suivant le choix du 7 novembre 2026.

Rôle : seconde implémentation du diagnostic Windows en lecture seule. Bibliothèque standard privilégiée et dépendances externes ajoutées seulement si nécessaires. Environnement virtuel propre au projet. Version Python maintenue à sélectionner au démarrage.

Structure prévue : `src/thorshield/{collectors,models,rules,reporting}`, commande CLI et `tests`. Interface graphique éventuelle après le MVP.

Terminé quand les contrôles Defender et pare-feu produisent le contrat commun, gèrent les accès refusés et correspondent aux observations Windows. Voir [le plan](../../docs/PLAN.md).
