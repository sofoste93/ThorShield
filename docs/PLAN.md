# Plan de construction

## 1. Mission et périmètre

Cible proposée : ce PC Windows, usage personnel, fonctionnement local. Édition, version et architecture de Windows à relever au début de l'implémentation. Interface initiale : ligne de commande en français et rapport JSON. Tableau de bord ensuite, une fois les informations fiables.

Objectif de la première version : répondre à « quelles protections sont actives, qu'est-ce qui a changé et que puis-je vérifier ? » avec des preuves datées et sans faux sentiment de sécurité.

## 2. Menaces et limites

| Situation | Apport prévu | Limite |
| --- | --- | --- |
| Protection désactivée ou état inaccessible | Signaler l'état et la source consultée | L'absence de lecture ne prouve pas une désactivation |
| Nouveau programme au démarrage | Comparer deux inventaires et expliquer la différence | Un changement n'est pas une preuve de logiciel malveillant |
| Modification de fichiers choisis | Comparer les empreintes avec une référence locale | Une référence déjà compromise ne garantit rien |
| Logiciel inattendu ou activité inhabituelle | Aider à examiner l'inventaire, extension ultérieure | Aucun verdict fondé seulement sur un nom ou une signature absente |
| Rançongiciel ou machine déjà compromise | Afficher les limites et orienter vers les protections existantes | ThorShield ne remplace ni antivirus, ni sauvegardes, ni réponse spécialisée |

Un attaquant avec les mêmes droits que l'utilisateur peut altérer les rapports locaux. Un attaquant administrateur peut tromper ou arrêter l'outil. Ces scénarios sont hors de la garantie du MVP.

## 3. Fonctionnalités par étape

### Étape A — Contrat et environnement

Relever Windows et les outils de développement disponibles. Écrire le format du rapport, trois jeux de données fictifs (normal, alerte, accès refusé), les règles initiales et les critères de comparaison. Choisir les versions maintenues des outils à cette date et verrouiller les dépendances.

Livrable : schéma JSON versionné, exemples synthétiques et protocole de validation.

### Étape B — MVP Java, lecture seule

- Lire l'état accessible de Microsoft Defender et des profils de pare-feu.
- Afficher source, horodatage, résultat et explication pour chaque contrôle.
- Différencier `ok`, `warning` et `unknown` ; ne jamais transformer une erreur en état rassurant.
- Produire un résumé français et un export JSON local à la demande.
- Fonctionner sous compte standard ; expliquer les contrôles inaccessibles.

Livrable : une commande manuelle reproductible. Aucun service au démarrage.

### Étape C — Parité Python puis Rust

Reproduire le MVP avec les mêmes entrées fictives et le même contrat. Comparer les observations normalisées, les erreurs et les alertes. Mesurer démarrage, mémoire et durée sur le même PC ; aucun langage n'est déclaré gagnant avant mesure.

Livrable : trois variantes indépendantes capables d'exécuter le même scénario. Une seule variante utilisée au quotidien.

### Étape D — Surveillance ciblée

Ajouter l'inventaire des points de démarrage explicitement pris en charge, puis la surveillance de fichiers choisis. Créer une référence seulement après validation de l'utilisateur. Gérer suppression, renommage, fichier verrouillé, chemins Unicode et changement pendant la lecture. Exclure par défaut secrets, profils de navigateur et dossiers système volumineux. Ne pas suivre automatiquement les jonctions ou liens hors du périmètre.

### Étape E — Expérience de bureau

Tableau de bord : état, observations, historique limité et recommandations. Notifications dédupliquées, sourçage des alertes et possibilité de suspendre l'observation. Choisir l'interface après comparaison des prototypes ; ne pas construire trois interfaces en parallèle au départ.

### Étape F — Actions et distribution

Seulement après validation des étapes précédentes : actions limitées, aperçu explicite, confirmation, élévation ponctuelle si nécessaire et retour arrière vérifiable. Prévoir paquet signé, provenance des dépendances, inventaire des composants, mises à jour authentifiées et désinstallation propre avant diffusion.

## 4. Hors périmètre initial

Moteur antivirus maison, pilotes noyau, interception TLS, collecte de mots de passe, analyse du contenu des communications, suppression automatique, quarantaine maison, arrêt automatique de processus et blocage réseau automatique. Aucun transfert vers un service cloud par défaut.

## 5. Critères d'acceptation

- Résultats cohérents avec les interfaces Windows consultées au même moment ; les divergences sont documentées.
- Même décision des trois variantes sur chaque jeu de données commun.
- Droits insuffisants, source indisponible, délai dépassé et données invalides donnent un résultat explicite et maîtrisé.
- Le MVP n'écrit que ses propres sorties demandées, sans modifier les paramètres Windows.
- Aucun secret ni contenu de document dans les rapports ou journaux ; chemins personnels masqués dans tout exemple partagé.
- Tests uniquement sur données fictives ou manipulations bénignes en environnement de test. Pas de désactivation des protections du PC pour démontrer une alerte.
- Objectifs provisoires : collecte simple sous 10 secondes et mémoire sous 200 Mo par variante sur le PC cible ; mesurer et ajuster après prototype, sans réduire la fiabilité pour tenir ces chiffres.

## 6. Risques de projet

Trois implémentations coûtent plus cher à maintenir : contrat partagé et développement successif. Les faux positifs fatiguent : messages descriptifs, déduplication et gravité justifiée. Les API et droits Windows varient : adaptation explicite et état inconnu. Un agent permanent augmente la surface d'attaque : reporter ce mode tant que le MVP manuel ne suffit pas à établir sa valeur.
