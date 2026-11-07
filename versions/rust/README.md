# ThorShield — Rust

[English](#english) | [Français](#français)

## English

Status: directory prepared; proposed third language, to be implemented after Java and Python.

Role: explore a native executable and measure its actual resource usage. Rust supports strongly structured states and error handling; it does not guarantee product security on its own. Select a stable toolchain and dependencies when implementation starts.

Planned structure: a Cargo project with `collectors`, `models`, `rules`, `reporting`, and `cli` modules. Encapsulate any Windows calls requiring `unsafe` within a small, documented, reviewed boundary. No kernel driver.

Complete when the contract and datasets match the other two variants, with time and memory measurements. See [the plan (French)](../../docs/PLAN.md).

## Français

Statut : emplacement préparé ; troisième langage proposé, implémentation après Java et Python.

Rôle : explorer un exécutable natif et mesurer son coût réel en ressources. Rust permet de structurer fortement les états et la gestion des erreurs ; il ne garantit pas à lui seul la sécurité du produit. Chaîne stable et dépendances à choisir au démarrage.

Structure prévue : projet Cargo, modules `collectors`, `models`, `rules`, `reporting`, `cli`. Encapsuler les éventuels appels Windows nécessitant `unsafe` dans une petite frontière documentée et revue. Aucun pilote noyau.

Terminé quand le contrat et les jeux de données correspondent aux deux autres variantes, avec mesures de temps et de mémoire. Voir [le plan](../../docs/PLAN.md).
