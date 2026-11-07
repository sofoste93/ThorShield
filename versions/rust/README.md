# ThorShield — Rust

Statut : emplacement préparé ; troisième langage proposé, implémentation après Java et Python.

Rôle : explorer un exécutable natif et mesurer son coût réel en ressources. Rust permet de structurer fortement les états et la gestion des erreurs ; il ne garantit pas à lui seul la sécurité du produit. Chaîne stable et dépendances à choisir au démarrage.

Structure prévue : projet Cargo, modules `collectors`, `models`, `rules`, `reporting`, `cli`. Encapsuler les éventuels appels Windows nécessitant `unsafe` dans une petite frontière documentée et revue. Aucun pilote noyau.

Terminé quand le contrat et les jeux de données correspondent aux deux autres variantes, avec mesures de temps et de mémoire. Voir [le plan](../../docs/PLAN.md).
