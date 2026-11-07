# Architecture et règles de sécurité

## Flux commun

Commande utilisateur → collecteurs Windows → observations normalisées → règles déterministes → rapport français et JSON.

Modules proposés dans chaque version : `collectors`, `models`, `rules`, `reporting`, `cli`. Les règles ne lancent aucune commande système ; elles reçoivent uniquement des observations. Les collecteurs ne décident pas seuls de la gravité.

## Contrat commun

Chaque rapport porte `schema_version`, `app_version`, `implementation`, `collected_at` (UTC), `observations`, `findings` et `errors`. Une observation contient un identifiant stable, sa source, sa date, son statut de collecte et sa valeur typée. Une alerte contient un identifiant de règle, sa gravité, les observations justificatives, un message et une suggestion.

Statuts de collecte : `success`, `unavailable`, `access_denied`, `timeout`, `invalid_data`. Évaluation : `ok`, `warning`, `unknown`. Un rapport partiel reste affichable mais ne doit jamais annoncer que tout va bien. Les différences de versions Windows et de fournisseur de protection doivent être représentées, pas masquées.

Une date de signature ancienne peut justifier une vérification, mais son seuil doit être documenté. Defender inactif ne signifie pas automatiquement absence d'antivirus : vérifier le contexte et les protections alternatives accessibles avant toute conclusion. Aucun score global de sécurité arbitraire dans le MVP.

## Intégration Windows

Privilégier les interfaces documentées. Un adaptateur PowerShell limité peut servir au prototype, avec script fixe, paramètres validés, sortie JSON, délai maximal et plafond de sortie. Ne jamais interpoler un chemin ou une donnée non fiable dans du code shell. Pas de téléchargement de script, de contournement de politique d'exécution ou d'exécution cachée persistante.

Avant implémentation, vérifier la disponibilité et les permissions des interfaces de lecture de Defender et du pare-feu pour l'édition Windows relevée. Les modes de Defender dépendent notamment des autres protections installées ; ThorShield ne doit pas tenter de les changer automatiquement.

## Données et permissions

Compte standard par défaut. Pas de demande d'administration globale au lancement. Aucun serveur réseau ou port d'écoute pour le MVP. Les rapports sont locaux, exportés explicitement, et peuvent révéler des chemins personnels : aperçu avant partage.

Pour l'historique futur : dossier dédié sous les données utilisateur, permissions restreintes, rotation et durée de conservation proposée de 30 jours configurable. Aucun fichier de rapport réel dans Git. La référence d'intégrité est locale et modifiable par l'utilisateur ; elle n'est pas une preuve inviolable. Les empreintes SHA-256 détectent une différence, pas la malveillance.

## Validation et livraison

Tests de règles sur données synthétiques partagées ; tests de contrat pour la parité ; tests d'intégration Windows séparés ; mesure des ressources ; vérification des dépendances avant chaque livraison. Tester les erreurs et la dégradation partielle aussi soigneusement que le cas nominal.

Avant une éventuelle version avec actions : établir une liste fermée d'opérations, vérifier à nouveau la cible au moment d'agir, rendre la portée visible et journaliser le résultat sans secrets. Les actions non réversibles exigent une conception distincte et ne font pas partie du plan MVP.

## Références officielles consultées le 5 novembre 2026

- [Compatibilité de Microsoft Defender Antivirus](https://learn.microsoft.com/en-us/defender-endpoint/microsoft-defender-antivirus-compatibility) : contexte des modes et interactions avec les autres produits.
- [Principe du moindre privilège](https://learn.microsoft.com/en-us/entra/identity-platform/secure-least-privileged-access) : base du choix de permissions minimales.
- [Protection contre les virus et menaces dans Sécurité Windows](https://support.microsoft.com/en-gb/windows/security/threat-malware-protection/virus-and-threat-protection-in-the-windows-security-app) : point de contrôle utilisateur.

Depuis le 7 novembre 2026, le prototype Java implémente la collecte Defender/pare-feu, les règles initiales et le rapport : voir `versions/java/README.md` et `shared/report.schema.json`. La surveillance de fichiers, l'historique, les actions et la distribution signée restent proposés, sans implémentation. Les versions Python et Rust restent à construire.
