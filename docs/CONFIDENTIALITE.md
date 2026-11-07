# Confidentialité du dépôt

Le dépôt contient le code, la documentation générique et des scénarios synthétiques. Les observations réelles d'un ordinateur ne sont pas des exemples publics.

## Conserver localement

- Rapports de diagnostic : dossiers `reports/` ou fichiers `diagnostic*.json`.
- Notes propres à une machine : dossier `private/`.
- Journaux, références d'intégrité, fichiers `.env`, clés et magasins de certificats.
- Réglages personnels des IDE, sorties de compilation et dumps mémoire.

Ces éléments sont exclus par `.gitignore`. Cela ne protège pas un fichier déjà suivi, ajouté avec `git add -f` ou copié sous un autre nom : relire le contenu de chaque commit et les pièces jointes avant publication.

## Identité des commits

Utiliser un pseudonyme public et une adresse GitHub `noreply` dans la configuration locale du dépôt, pour l'auteur comme pour le committer. Ne pas publier l'adresse de messagerie personnelle. Le pseudonyme et l'historique des commits restent publics après publication.

Référence : [adresse de commit et confidentialité GitHub](https://docs.github.com/en/account-and-profile/how-tos/email-preferences/setting-your-commit-email-address).

## Portée

Ce projet est un prototype de diagnostic, sans garantie de protection exhaustive. La relecture du dépôt ne garantit pas l'absence de toute vulnérabilité. Une éventuelle publication doit porter uniquement sur les fichiers suivis, jamais sur une archive complète du dossier de travail contenant les données locales.

La licence de réutilisation du code reste à choisir avant de présenter le projet comme open source sous une licence particulière.
