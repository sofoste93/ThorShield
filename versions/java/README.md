# ThorShield — Java 0.1.0

Premier prototype CLI Windows, Java 21 et Maven. Lit Defender et la configuration effective des profils Domain, Private et Public du pare-feu. Aucun réglage modifié, aucun service installé, aucun envoi réseau par l'application. Export JSON facultatif, sans écrasement d'un fichier existant.

## Ouvrir dans IntelliJ IDEA

Ouvrir le `pom.xml` de ce dossier comme projet Maven. Sélectionner un JDK 21 pour le projet **et pour Maven**. Vérifier `java -version` et `mvn -version` dans le terminal utilisé.

## Compiler et tester dans PowerShell

```powershell
Set-Location .\versions\java
# JAVA_HOME doit pointer vers votre installation du JDK 21.
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn verify
java -jar target\thorshield.jar --help
java -jar target\thorshield.jar
```

Ces variables ne changent que le terminal courant. La première compilation télécharge les dépendances Maven. Le JAR généré embarque Gson pour fonctionner ensuite sans Maven.

Export explicite vers un fichier neuf dans un dossier existant :

```powershell
New-Item -ItemType Directory -Force reports | Out-Null
java -jar target\thorshield.jar --json reports\diagnostic.json
```

Codes de sortie : **0** contrôles évalués OK, **1** au moins un point à vérifier, **2** au moins une information inconnue (prioritaire sur 1), **3** erreur d'arguments ou d'export. Même un code 0 ne signifie pas que le PC est exempt de menace.

## Comportement et limites

- `ok` : seuls les indicateurs vérifiés sont actifs.
- `warning` : protection Defender inactive en mode Normal ou profil de pare-feu désactivé.
- `unknown` : accès refusé, source absente, données incorrectes, délai dépassé ou mode Defender autre que Normal.
- Les antivirus tiers ne sont pas évalués : un mode Defender passif reste explicitement inconnu.
- Les profils affichés ne sont pas nécessairement ceux du réseau actuel. Les règles de filtrage elles-mêmes ne sont pas auditées.
- Deux collectes indépendantes, délai maximal de 8 secondes chacune et sortie plafonnée à 64 Kio. Un échec n'efface pas les résultats de l'autre source.
- PowerShell Windows lancé sans profil et sans contournement de politique d'exécution, via un script fixe embarqué. Aucun argument utilisateur interpolé dans le script.
- Pas encore de vérification des signatures récentes, mises à jour Windows, programmes au démarrage ou fichiers.

## Contrat et tests

Schéma du rapport : `../../shared/report.schema.json`. Scénarios synthétiques : `../../shared/fixtures`. Les tests Maven couvrent les règles, données invalides, erreurs de collecte, sortie de processus excessive et délai dépassé. Ils ne désactivent aucune protection et ne lisent pas l'état réel du PC.

Les scénarios communs prépareront les comparaisons Python/Rust. Les rapports réels restent dans `reports/`, ignoré par Git.

Sources : [Get-MpComputerStatus](https://learn.microsoft.com/en-us/powershell/module/defender/get-mpcomputerstatus), [Get-NetFirewallProfile](https://learn.microsoft.com/en-us/powershell/module/netsecurity/get-netfirewallprofile), [Gson](https://github.com/google/gson/releases). Versions des dépendances et plugins fixées dans le POM ; cela ne constitue pas encore un audit complet de la chaîne de distribution.
