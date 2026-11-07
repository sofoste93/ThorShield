# ThorShield — Java 0.1.0

[English](#english) | [Français](#français)

## English

Initial Windows CLI prototype using Java 21 and Maven. Reads Defender and the effective configuration of the Domain, Private, and Public firewall profiles. The application changes no settings, installs no service, and sends no network requests. Optional JSON export never overwrites an existing file.

### Open in IntelliJ IDEA

Open this directory's `pom.xml` as a Maven project. Select JDK 21 for both **the project and Maven**. Check `java -version` and `mvn -version` in the terminal you use.

### Build and test in PowerShell

From the repository root:

```powershell
Set-Location .\versions\java
# JAVA_HOME must point to your JDK 21 installation.
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn verify
java -jar target\thorshield.jar --help
java -jar target\thorshield.jar
```

If your terminal is already in `versions/java`, skip the first command. These variables affect only the current terminal. The initial build downloads Maven dependencies. The generated JAR bundles Gson so it can subsequently run without Maven. CLI messages are currently in French; these bilingual guides do not change the application's language.

Explicit export to a new file in an existing directory:

```powershell
New-Item -ItemType Directory -Force reports | Out-Null
java -jar target\thorshield.jar --json reports\diagnostic.json
```

Exit codes: **0** evaluated checks OK, **1** at least one item to review, **2** at least one unknown result (takes precedence over 1), **3** argument or export error. Even code 0 does not mean the PC is free of threats.

### Behavior and limitations

- `ok`: only the checked indicators are active.
- `warning`: a Defender protection is inactive in Normal mode, or a firewall profile is disabled.
- `unknown`: access denied, missing source, invalid data, timeout, or a Defender mode other than Normal.
- Third-party antivirus products are not evaluated: passive Defender mode remains explicitly unknown.
- Displayed profiles are not necessarily those of the current network. Filtering rules themselves are not audited.
- Two independent collections, each limited to 8 seconds and 64 KiB of output. Failure of one source does not discard the other source's results.
- Windows PowerShell runs without a profile or execution-policy bypass, using a fixed embedded script. No user argument is interpolated into that script.
- No checks yet for recent signatures, Windows updates, startup programs, or files.

### Contract and tests

Report schema: `../../shared/report.schema.json`. Synthetic scenarios: `../../shared/fixtures`. Maven tests cover rules, invalid data, collection errors, excessive process output, and timeouts. They neither disable protections nor read the PC's actual security state.

The shared scenarios prepare future Python/Rust comparisons. Actual reports remain in `reports/`, which Git ignores.

Sources: [Get-MpComputerStatus](https://learn.microsoft.com/en-us/powershell/module/defender/get-mpcomputerstatus), [Get-NetFirewallProfile](https://learn.microsoft.com/en-us/powershell/module/netsecurity/get-netfirewallprofile), [Gson](https://github.com/google/gson/releases). Dependency and plugin versions are pinned in the POM; this is not a full software supply-chain audit.

## Français

Premier prototype CLI Windows, Java 21 et Maven. Lit Defender et la configuration effective des profils Domain, Private et Public du pare-feu. Aucun réglage modifié, aucun service installé, aucun envoi réseau par l'application. Export JSON facultatif, sans écrasement d'un fichier existant.

### Ouvrir dans IntelliJ IDEA

Ouvrir le `pom.xml` de ce dossier comme projet Maven. Sélectionner un JDK 21 pour le projet **et pour Maven**. Vérifier `java -version` et `mvn -version` dans le terminal utilisé.

### Compiler et tester dans PowerShell

Depuis la racine du dépôt :

```powershell
Set-Location .\versions\java
# JAVA_HOME doit pointer vers votre installation du JDK 21.
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn verify
java -jar target\thorshield.jar --help
java -jar target\thorshield.jar
```

Si le terminal est déjà dans `versions/java`, ignorer la première commande. Ces variables ne changent que le terminal courant. La première compilation télécharge les dépendances Maven. Le JAR généré embarque Gson pour fonctionner ensuite sans Maven. Les messages de la CLI sont actuellement en français ; ces guides bilingues ne changent pas la langue de l'application.

Export explicite vers un fichier neuf dans un dossier existant :

```powershell
New-Item -ItemType Directory -Force reports | Out-Null
java -jar target\thorshield.jar --json reports\diagnostic.json
```

Codes de sortie : **0** contrôles évalués OK, **1** au moins un point à vérifier, **2** au moins une information inconnue (prioritaire sur 1), **3** erreur d'arguments ou d'export. Même un code 0 ne signifie pas que le PC est exempt de menace.

### Comportement et limites

- `ok` : seuls les indicateurs vérifiés sont actifs.
- `warning` : protection Defender inactive en mode Normal ou profil de pare-feu désactivé.
- `unknown` : accès refusé, source absente, données incorrectes, délai dépassé ou mode Defender autre que Normal.
- Les antivirus tiers ne sont pas évalués : un mode Defender passif reste explicitement inconnu.
- Les profils affichés ne sont pas nécessairement ceux du réseau actuel. Les règles de filtrage elles-mêmes ne sont pas auditées.
- Deux collectes indépendantes, délai maximal de 8 secondes chacune et sortie plafonnée à 64 Kio. Un échec n'efface pas les résultats de l'autre source.
- PowerShell Windows lancé sans profil et sans contournement de politique d'exécution, via un script fixe embarqué. Aucun argument utilisateur interpolé dans le script.
- Pas encore de vérification des signatures récentes, mises à jour Windows, programmes au démarrage ou fichiers.

### Contrat et tests

Schéma du rapport : `../../shared/report.schema.json`. Scénarios synthétiques : `../../shared/fixtures`. Les tests Maven couvrent les règles, données invalides, erreurs de collecte, sortie de processus excessive et délai dépassé. Ils ne désactivent aucune protection et ne lisent pas l'état réel du PC.

Les scénarios communs prépareront les comparaisons Python/Rust. Les rapports réels restent dans `reports/`, ignoré par Git.

Sources : [Get-MpComputerStatus](https://learn.microsoft.com/en-us/powershell/module/defender/get-mpcomputerstatus), [Get-NetFirewallProfile](https://learn.microsoft.com/en-us/powershell/module/netsecurity/get-netfirewallprofile), [Gson](https://github.com/google/gson/releases). Versions des dépendances et plugins fixées dans le POM ; cela ne constitue pas encore un audit complet de la chaîne de distribution.
