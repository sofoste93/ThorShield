# Validation du prototype Java 0.1.0

La compilation Maven et les 24 tests automatisés ont réussi. Les tests utilisent des données synthétiques : cas nominal, avertissements, accès refusé, mode passif, données invalides, limites de processus et protection des exports.

Le JAR autonome, son aide et le refus d'écraser un rapport existant ont été vérifiés. Le format d'un export a été contrôlé avec le schéma JSON commun. La validation du schéma n'est pas encore intégrée à Maven.

Des lectures Windows ont également été effectuées sans modification des protections. Les détails de machine, mesures et résultats de sécurité restent dans les notes privées, hors du dépôt. Aucun rapport réel n'est inclus ici.

## Reproduire

Avec Java 21 et Maven, depuis `versions/java`, lancer `mvn verify`. Les tests ne consultent pas la sécurité du PC. Le diagnostic réel se lance séparément suivant le guide Java.

## Limites

- Une source lente peut dépasser son délai : le rapport indique alors une information inconnue.
- La comparaison avec l'interface graphique Sécurité Windows reste à réaliser.
- Les antivirus tiers ne sont pas évalués.
- Pas encore d'audit complet des dépendances, de paquet signé, de mesure systématique de mémoire ou de garantie de performance.
- La parité Python/Rust reste à réaliser.
